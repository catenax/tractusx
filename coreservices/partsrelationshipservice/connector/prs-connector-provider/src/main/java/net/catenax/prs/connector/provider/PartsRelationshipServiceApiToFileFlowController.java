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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.prs.client.ApiException;
import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;

import java.util.Optional;

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
     * Blob storage client
     */
    private final BlobStorageClient blobStorageClient;

    /**
     * @param monitor   Logger
     * @param prsClient Client used to call PRS API
     * @param vault Vault
     * @param blobStorageClient Blob storage client
     */
    public PartsRelationshipServiceApiToFileFlowController(final Monitor monitor, final PartsRelationshipServiceApi prsClient, final Vault vault, final BlobStorageClient blobStorageClient) {
        this.monitor = monitor;
        this.prsClient = prsClient;
        this.vault = vault;
        this.blobStorageClient = blobStorageClient;
    }

    @Override
    public boolean canHandle(final DataRequest dataRequest) {
        return AzureBlobStoreSchema.TYPE.equalsIgnoreCase(dataRequest.getDataDestination().getType());
    }

    @Override
    public DataFlowInitiateResponse initiateFlow(final DataRequest dataRequest) {
        // verify partsTreeRequest
        final var serializedRequest = dataRequest.getProperties().get("prs-request-parameters");
        final var destinationPath = dataRequest.getProperties().get("prs-destination-path");

        // Read API Request from message payload
        PartsTreeByObjectIdRequest request;
        monitor.info("Received request " + serializedRequest + " with destination path " + destinationPath);
        try {
            request = MAPPER.readValue(serializedRequest, PartsTreeByObjectIdRequest.class);
            monitor.info("request with " + request.getObjectIDManufacturer());
        } catch (JsonProcessingException e) {
            final String message = "Error deserializing " + PartsTreeByObjectIdRequest.class.getName() + ": " + e.getMessage();
            monitor.severe(message, e);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        // call API
        final PartRelationshipsWithInfos response;
        try {
            response = prsClient.getPartsTreeByOneIdAndObjectId(request.getOneIDManufacturer(), request.getObjectIDManufacturer(),
                    request.getView(), request.getAspect(), request.getDepth());
        } catch (ApiException e) {
            final String message = "Error with API call: " + e.getMessage();
            monitor.severe(message, e);
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
        final var destSecretName = dataRequest.getDataDestination().getKeyName();
        if (destSecretName == null) {
            monitor.severe(format("No credentials found for %s, will not copy!", dataRequest.getDestinationType()));
            return new DataFlowInitiateResponse(ResponseStatus.ERROR_RETRY, "Did not find credentials for data destination");
        }
        final var sasToken = vault.resolveSecret(destSecretName);

        // write API response to blob storage
        try {
            blobStorageClient.writeToBlob(dataRequest.getDataDestination(), destinationPath, partRelationshipsWithInfos, sasToken);
        } catch (EdcException e) {
            String message = "Data transfer to Azure Blob Storage failed";
            monitor.severe(message, e);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        return DataFlowInitiateResponse.OK;
    }
}
