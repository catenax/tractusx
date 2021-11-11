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
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
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
    private final BlobStoreApi blobApi;
    private final String storageAccountName;

    public ConsumerApiController(Monitor monitor, TransferProcessManager processManager, TransferProcessStore processStore, BlobStoreApi blobApi,
                                 String storageAccountName) {
        this.monitor = monitor;
        this.processManager = processManager;
        this.processStore = processStore;
        this.blobApi = blobApi;
        this.storageAccountName = storageAccountName;
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
     * @return TransferInitiateResponse with process id.
     */
    @POST
    @Path("file/{filename}")
    public Response initiateTransfer(@PathParam("filename") String filename, @QueryParam("connectorAddress") String connectorAddress) {

        monitor.info(format("Received request for file %s against provider %s", filename, connectorAddress));

        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(connectorAddress, "connectorAddress");

        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(connectorAddress) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id(filename) // this is an id of an entry from artifact catalog, only test-document available
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE) //the provider uses this to select the correct DataFlowController
                        .property("account", storageAccountName)
                        .build())
                .managedResources(true) //we do not need any provisioning
                .build();

        var response = processManager.initiateConsumerRequest(dataRequest);
        return response.getStatus() != ResponseStatus.OK ? Response.status(400).build() : Response.ok(response.getId()).build();
    }

    @GET
    @Path("datarequest/{id}/state")
    public Response getStatus(@PathParam("id") String requestId) {
        monitor.info("getting status of data request " + requestId);

        var process = processStore.find(requestId);
        if (process == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (process.getState() == TransferProcessStates.COMPLETED.code()) {
            return Response.ok(createSasUrl(process)).build();
        }

        return Response.accepted(TransferProcessStates.from(process.getState()).toString()).build();
    }

    @NotNull
    private String createSasUrl(TransferProcess process) {
        var containerName = process.getDataRequest().getDataDestination().getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        var filename = process.getDataRequest().getDataEntry().getId() + ".complete";
        var sasToken = blobApi.createContainerSasToken(storageAccountName, containerName, "r", OffsetDateTime.now().plusHours(1));
        monitor.debug("Temporary SAS token (read-only) created");
        return buildSasUrl(containerName, filename, sasToken, storageAccountName);
    }

    @NotNull
    private String buildSasUrl(String containerName, String filename, String sasToken, String storageAccountName) {
        return "https://" + storageAccountName + ".blob.core.windows.net/" + containerName + "/" + filename + "?" + sasToken;
    }
}
