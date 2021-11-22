package net.catenax.prs.connector.provider;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.eclipse.dataspaceconnector.provision.azure.AzureSasToken;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.String.format;

public class BlobStorageClient {

    /**
     * Type manager to deserialize SAS token
     */
    private final TypeManager typeManager;
    /**
     * Vault to retrieve secret to access blob storage
     */
    private final Vault vault;

    public BlobStorageClient(TypeManager typeManager,Vault vault) {
        this.typeManager = typeManager;
        this.vault = vault;
    }

    public void writeToBlob(final DataAddress destination, final String blobName, final String data) {
        final var containerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        final var accountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        final var destSecretName = destination.getKeyName();

        final var sasToken = getAzureSasToken(destSecretName);
        final var blobClient = getBlobClient(blobName, containerName, accountName, sasToken);
        byte[] bytes = data.getBytes();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(bytes)) {
            blobClient.upload(dataStream, bytes.length);
        } catch (IOException e) {
            throw new EdcException(e);
        }
    }

    private AzureSasToken getAzureSasToken(String destSecretName) {
        try {
            final var secret = vault.resolveSecret(destSecretName);
            return typeManager.readValue(secret, AzureSasToken.class);
        } catch (Exception e) {
            throw new EdcException("Can not retrieve SAS token", e);
        }
    }

    private BlobClient getBlobClient(String blobName, String containerName, String accountName, AzureSasToken sasToken) {
        return new BlobClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .sasToken(sasToken.getSas())
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();
    }
}
