package org.eclipse.dataspaceconnector.extensions.api;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.eclipse.dataspaceconnector.provision.azure.AzureSasToken;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

public class BlobDataFlowController implements DataFlowController {
    private final Vault vault;
    private final Monitor monitor;
    private final TypeManager typeManager;

    public BlobDataFlowController(Vault vault, Monitor monitor, TypeManager typeManager) {

        this.vault = vault;
        this.monitor = monitor;
        this.typeManager = typeManager;
    }

    @Override
    public boolean canHandle(DataRequest dataRequest) {

        String sourceType = dataRequest.getDataEntry().getCatalogEntry().getAddress().getType();
        String destinationType = dataRequest.getDestinationType();

        return sourceType.equals("File") && destinationType.equals("AzureStorage");
    }

    @Override
    public @NotNull DataFlowInitiateResponse initiateFlow(DataRequest dataRequest) {
        DataAddress source = dataRequest.getDataEntry().getCatalogEntry().getAddress();
        String sourceType = source.getType();
        String destinationType = dataRequest.getDestinationType();

        var destSecretName = dataRequest.getDataDestination().getKeyName();
        if (destSecretName == null) {
            monitor.severe(format("No credentials found for %s, will not copy!", destinationType));
            return new DataFlowInitiateResponse(ResponseStatus.ERROR_RETRY, "Did not find credentials for data destination.");
        }
        // not needed if we use BlobStoreApi and account storage key
        var secret = vault.resolveSecret(destSecretName);

        monitor.info(format("Copying data from %s to %s", sourceType, destinationType));

        // verify source path
        String sourceFileName = source.getProperty("filename");
        var sourcePath = Path.of(source.getProperty("path"), sourceFileName);
        if (!sourcePath.toFile().exists()) {
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, "source file " + sourcePath + " does not exist!");
        }

        try {
            write(dataRequest.getDataDestination(), dataRequest.getDataEntry().getId(), Files.readAllBytes(sourcePath), secret);
        } catch (IOException e) {
            monitor.severe("Error when reading data", e);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, "Error when reading data" + e.getMessage());
        }

        return DataFlowInitiateResponse.OK;
    }

    public void write(DataAddress destination, String name, byte[] data, String secretToken) {

        var containerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        var accountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        var blobName = destination.getProperty(AzureBlobStoreSchema.BLOB_NAME) == null ? name : destination.getProperty(AzureBlobStoreSchema.BLOB_NAME);
        var sasToken = typeManager.readValue(secretToken, AzureSasToken.class);

        BlobClient blobClient = new BlobClientBuilder()
                .endpoint("https://"+accountName+".blob.core.windows.net")
                .sasToken(sasToken.getSas())
                .containerName(containerName)
                .blobName(blobName + ".complete") // needed because the way ObjectContainerStatusChecker checks if process is complete
                .buildClient();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(data)) {
            blobClient.upload(dataStream, data.length);
            monitor.info("File uploaded to Azure storage");
        } catch (IOException e) {
            monitor.severe("Data transfer to Azure Blob Storage failed", e);
        }

    }

}
