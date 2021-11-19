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

import com.azure.storage.blob.BlobClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.prs.client.ApiException;
import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.String.format;

/**
 * Handles a data flow to call PRS API and save the result to a file.
 */
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class PartsRelationshipServiceApiToFileFlowController implements DataFlowController {

    /**
     * JSON serializer / deserializer.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Logger.
     */
    private final Monitor monitor;

    /**
     * Client stub to call PRS API.
     */
    private final PartsRelationshipServiceApi prsClient;

    /**
     * Vault to retrieve secret to access blob storage
     */
    private final Vault vault;

    /**
     * Type manager to deserialize SAS token
     */
    private final TypeManager typeManager;

    /**
     * @param monitor   Logger
     * @param prsClient Client used to call PRS API
     * @param vault Vault
     * @param typeManager Type manager
     */
    public PartsRelationshipServiceApiToFileFlowController(final Monitor monitor, final PartsRelationshipServiceApi prsClient, final Vault vault, final TypeManager typeManager) {
        this.monitor = monitor;
        this.prsClient = prsClient;
        this.vault = vault;
        this.typeManager = typeManager;
    }

    @Override
    public boolean canHandle(final DataRequest dataRequest) {
        // temporary assignment to handle AzureStorage until proper flow controller
        // is implemented in [A1MTDC-165]
        return "AzureStorage".equalsIgnoreCase(dataRequest.getDataDestination().getType());
    }

    @Override
    public DataFlowInitiateResponse initiateFlow(final DataRequest dataRequest) {
        // verify partsTreeRequest
        final String serializedRequest = dataRequest.getProperties().get("prs-request-parameters");
        final String destinationPath = dataRequest.getProperties().get("prs-destination-path");

        // Read API Request from message payload
        PartsTreeByObjectIdRequest request;
        monitor.info("Received request " + serializedRequest + " with destination path " + destinationPath);
        try {
            request = MAPPER.readValue(serializedRequest, PartsTreeByObjectIdRequest.class);
            monitor.info("request with " + request.getObjectIDManufacturer());
        } catch (JsonProcessingException e) {
            final String message = "Error deserializing " + PartsTreeByObjectIdRequest.class.getName() + ": " + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        // call API
        final PartRelationshipsWithInfos response;
        try {
            response = prsClient.getPartsTreeByOneIdAndObjectId(request.getOneIDManufacturer(), request.getObjectIDManufacturer(),
                    request.getView(), request.getAspect(), request.getDepth());
        } catch (ApiException e) {
            final String message = "Error with API call: " + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        // serialize API response
        final String partRelationshipsWithInfos;
        try {
            partRelationshipsWithInfos = MAPPER.writeValueAsString(response);
            // We suspect the connectorSystemTests to be flaky when running right after the deployment workflow.
            // The issue is hard to reproduce. Login the PRS response, to help when this will happen again.
            monitor.info(format("partRelationshipsWithInfos: %s", partRelationshipsWithInfos));
        } catch (JsonProcessingException e) {
            final String message = "Error serializing API response: " + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        // Retrieve blob storage SAS token from vault
        var destSecretName = dataRequest.getDataDestination().getKeyName();
        if (destSecretName == null) {
            monitor.severe(format("No credentials found for %s, will not copy!", dataRequest.getDestinationType()));
            return new DataFlowInitiateResponse(ResponseStatus.ERROR_RETRY, "Did not find credentials for data destination.");
        }
        var secret = vault.resolveSecret(destSecretName);

        // write API response to blob storage
        write(dataRequest.getDataDestination(), destinationPath, partRelationshipsWithInfos.getBytes(), secret);

        return DataFlowInitiateResponse.OK;
    }

    public void write(final DataAddress destination, final String blobName, final byte[] data, final String secretToken) {
        var containerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        var accountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        var sasToken = typeManager.readValue(secretToken, AzureSasToken.class);

        var blobClient = new BlobClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .sasToken(sasToken.getSas())
                .containerName(containerName)
                .blobName(blobName + ".complete") // ".complete" suffix needed by ObjectContainerStatusChecker
                .buildClient();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(data)) {
            blobClient.upload(dataStream, data.length);
            monitor.info("File uploaded to Azure storage");
        } catch (IOException e) {
            monitor.severe("Data transfer to Azure Blob Storage failed", e);
        }

    }
}
