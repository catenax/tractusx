package net.catenax.prs.connector.provider;

import com.azure.storage.blob.BlobClientBuilder;
import net.catenax.prs.connector.monitor.LoggerMonitor;
import org.eclipse.dataspaceconnector.provision.azure.AzureSasToken;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BlobStorageClient {

    /**
     * Type manager to deserialize SAS token
     */
    private final TypeManager typeManager;
    private final Monitor monitor;

    public BlobStorageClient(TypeManager typeManager, Monitor monitor) {
        this.typeManager = typeManager;
        this.monitor = monitor;
    }

    public void writeToBlob(final DataAddress destination, final String blobName, final String data, final String secretToken) {
        final var containerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        final var accountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        final var sasToken = typeManager.readValue(secretToken, AzureSasToken.class);

        final var blobClient = new BlobClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .sasToken(sasToken.getSas())
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();

        byte[] bytes = data.getBytes();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(bytes)) {
            blobClient.upload(dataStream, bytes.length);
            monitor.info("File uploaded to Azure storage");
        } catch (IOException e) {
            throw new EdcException(e);
        }
    }
}
