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
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * Blob storage client for consumer connector
 */
public class BlobStorageClient {
    /**
     * Logger.
     */
    private final Monitor monitor;
    /**
     * Type manager to deserialize SAS token
     */
    private final TypeManager typeManager;
    /**
     * Vault to retrieve secret to access blob storage
     */
    private final Vault vault;
    /**
     * Factory for BlobClient
     */
    private final BlobClientFactory blobClientFactory;

    /**
     * @param monitor     Logger
     * @param typeManager Type manager
     * @param vault       Vault
     */
    public BlobStorageClient(final Monitor monitor, final TypeManager typeManager, final Vault vault) {
        this(monitor, typeManager, vault, new BlobClientFactory());
    }

    /**
     * Constructor used in tests
     *
     * @param monitor           Logger
     * @param typeManager       Type manager
     * @param vault             Vault
     * @param blobClientFactory Blob client factory
     */
    /* package */ BlobStorageClient(final Monitor monitor, final TypeManager typeManager, final Vault vault, final BlobClientFactory blobClientFactory) {
        this.monitor = monitor;
        this.typeManager = typeManager;
        this.vault = vault;
        this.blobClientFactory = blobClientFactory;
    }

    /**
     * Writes data into a blob
     *
     * @param destination Data destination specifying account and container name
     * @param blobName    Blob name
     * @param data        Data to write
     */
    public void writeToBlob(final DataAddress destination, final String blobName, final String data) {
        final var containerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        final var accountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        final var destSecretName = destination.getKeyName();

        final var sasToken = getAzureSasToken(destSecretName);
        final var blobClient = blobClientFactory.getBlobClient(blobName, containerName, accountName, sasToken);
        final byte[] bytes = data.getBytes();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(bytes)) {
            blobClient.upload(dataStream, bytes.length, true);
        } catch (IOException e) {
            throw new EdcException(e);
        }
        monitor.info(format(
                "File uploaded to Azure storage account '%s', container '%s', blob '%s'",
                accountName, containerName, blobName));
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    private AzureSasToken getAzureSasToken(final String destSecretName) {
        final var secret = ofNullable(vault.resolveSecret(destSecretName))
                .orElseThrow(() -> new EdcException("Can not retrieve SAS token"));
        try {
            return typeManager.readValue(secret, AzureSasToken.class);
        } catch (Exception e) {
            throw new EdcException("Invalid SAS token", e);
        }
    }

    /**
     * XXX.
     */
    /* package */ static class BlobClientFactory {
        /**
         * XXX.
         * @param blobName XXX.
         * @param containerName XXX.
         * @param accountName XXX.
         * @param sasToken XXX.
         * @return XXX.
         */
        public BlobClient getBlobClient(
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
}
