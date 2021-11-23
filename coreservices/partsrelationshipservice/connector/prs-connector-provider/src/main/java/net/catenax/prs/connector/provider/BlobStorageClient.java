//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.provider;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.eclipse.dataspaceconnector.provision.azure.AzureSasToken;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * XXX
 */
public class BlobStorageClient {

    /**
     * Type manager to deserialize SAS token
     */
    private final TypeManager typeManager;
    /**
     * Vault to retrieve secret to access blob storage
     */
    private final Vault vault;

    /**
     * XXX
     * @param typeManager XXX
     * @param vault XXX
     */
    public BlobStorageClient(final TypeManager typeManager, final Vault vault) {
        this.typeManager = typeManager;
        this.vault = vault;
    }

    /**
     * XXX
     * @param destination XXX
     * @param blobName XXX
     * @param data XXX
     */
    public void writeToBlob(final DataAddress destination, final String blobName, final String data) {
        final var containerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        final var accountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        final var destSecretName = destination.getKeyName();

        final var sasToken = getAzureSasToken(destSecretName);
        final var blobClient = getBlobClient(blobName, containerName, accountName, sasToken);
        final byte[] bytes = data.getBytes();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(bytes)) {
            blobClient.upload(dataStream, bytes.length);
        } catch (IOException e) {
            throw new EdcException(e);
        }
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    private AzureSasToken getAzureSasToken(final String destSecretName) {
        try {
            final var secret = vault.resolveSecret(destSecretName);
            return typeManager.readValue(secret, AzureSasToken.class);
        } catch (Exception e) {
            throw new EdcException("Can not retrieve SAS token", e);
        }
    }

    private BlobClient getBlobClient(
            final String blobName,
            final String containerName,
            final String accountName,
            final AzureSasToken sasToken) {
        return new BlobClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .sasToken(sasToken.getSas())
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();
    }
}
