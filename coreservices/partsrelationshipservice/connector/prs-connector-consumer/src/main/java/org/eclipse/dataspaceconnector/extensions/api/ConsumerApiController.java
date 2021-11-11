//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package org.eclipse.dataspaceconnector.extensions.api;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;

import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

/**
 * Consumer API Controller.
 * Provides consumer extra endpoints.
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/")
public class ConsumerApiController {

    private final Monitor monitor;
    private final TransferProcessManager processManager;
    private final TransferProcessStore processStore;

    /**
     * @param monitor This is a logger.
     * @param processManager Process manager responsible for sending messages to provider.
     * @param processStore Manages storage of TransferProcess state.
     */
    public ConsumerApiController(Monitor monitor, TransferProcessManager processManager, TransferProcessStore processStore) {
        this.monitor = monitor;
        this.processManager = processManager;
        this.processStore = processStore;
    }

    /**
     * Health endpoint.
     * @return Consumer status
     */
    @GET
    @Path("health")
    public String checkHealth() {
        monitor.info("%s :: Received a health request");
        return "I'm alive!";
    }

    /**
     * Endpoint to trigger a request, so that a file get copied into a specific destination.
     * @param filename Path of file source.
     * @param connectorAddress Provider connector address to send the message to.
     * @param destinationPath Destination path where the file should be copied.
     * @return TransferInitiateResponse with process id.
     */
    @POST
    @Path("file/{filename}")
    public Response initiateTransfer(@PathParam("filename") String filename, @QueryParam("connectorAddress") String connectorAddress,
                                     @QueryParam("destination") String destinationPath) {

        monitor.info(format("Received request for file %s against provider %s", filename, connectorAddress));

        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(connectorAddress, "connectorAddress");

        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(connectorAddress) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id(filename)
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type("File") //the provider uses this to select the correct DataFlowController
                        .property("path", destinationPath) //where we want the file to be stored
                        .build())
                .managedResources(false) //we do not need any provisioning
                .build();

        var response = processManager.initiateConsumerRequest(dataRequest);
        return response.getStatus() != ResponseStatus.OK ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(response.getId()).build();
    }

    /**
     * Provides status of a process
     * @param requestId If of the process
     * @return Process state
     */
    @GET
    @Path("datarequest/{id}/state")
    public Response getStatus(@PathParam("id") String requestId) {
        monitor.info("Getting status of data request " + requestId);

        var process = processStore.find(requestId);
        if (process == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(TransferProcessStates.from(process.getState()).toString()).build();
    }
}
