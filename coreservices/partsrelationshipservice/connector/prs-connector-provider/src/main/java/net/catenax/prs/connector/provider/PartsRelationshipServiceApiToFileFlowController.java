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
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
     * @param monitor   Logger
     * @param prsClient Client used to call PRS API
     */
    public PartsRelationshipServiceApiToFileFlowController(final Monitor monitor, final PartsRelationshipServiceApi prsClient) {
        this.monitor = monitor;
        this.prsClient = prsClient;
    }

    @Override
    public boolean canHandle(final DataRequest dataRequest) {
        return "file".equalsIgnoreCase(dataRequest.getDataDestination().getType());
    }

    @Override
    public DataFlowInitiateResponse initiateFlow(final DataRequest dataRequest) {
        // verify partsTreeRequest
        final String serializedRequest = dataRequest.getDataDestination().getProperty("request");

        // Read API Request from message payload

        PartsTreeByObjectIdRequest request;
        monitor.info("Received request " + serializedRequest);
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
        } catch (JsonProcessingException e) {
            final String message = "Error serializing API response: " + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        // write API response to file

        final var destinationPath = Path.of(dataRequest.getDataDestination().getProperty("path"));
        try {
            Files.writeString(destinationPath, partRelationshipsWithInfos);
        } catch (IOException e) {
            final String message = "Error writing file " + destinationPath + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        return DataFlowInitiateResponse.OK;
    }
}
