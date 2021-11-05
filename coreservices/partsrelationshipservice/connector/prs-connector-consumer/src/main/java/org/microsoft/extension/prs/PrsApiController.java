package org.microsoft.extension.prs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;

import java.util.Objects;
import java.util.UUID;

/**
 * This class contains endpoints added to the consumer, so that a PRS client can request parts tree through consumer.
 * This class should not contain PRS business logic. The business logic of PRS should be inside the PRS application.
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/")
public class PrsApiController {

    // Monitor is a logger.
    private final Monitor monitor;
    private final TransferProcessManager processManager;

    public PrsApiController(Monitor monitor, TransferProcessManager processManager) {
        this.monitor = monitor;
        this.processManager = processManager;
    }

    @GET
    @Path("health")
    public String checkHealth() {
        monitor.info("Received a health request");
        return "Consumer is healthy.";
    }

    @GET
    @Path("vins/{vin}/partsTree")
    public Response transferQuery(@PathParam("vin") String vin,
                                  @QueryParam("view") String view,
                                  // provider address we want to ask the data to.
                                  @QueryParam("connectorAddress") String connectorAddress,
                                  // destination where the result should be stored.
                                  @QueryParam("destination") String destinationPath) {

        monitor.info("Received a request with vin:" + vin);
        monitor.info("Provider connector address:" + connectorAddress);

        Objects.requireNonNull(vin, "vin");
        Objects.requireNonNull(view, "view");
        Objects.requireNonNull(connectorAddress, "connectorAddress");
        Objects.requireNonNull(destinationPath, "destination");


        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(connectorAddress) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id("prs-api") // TODO: See what id we use as vin will also be in property.
                        // TODO: Make sure catalogEntry is what needs to be used for property or if there is another
                        //  concept/object that could be used.
                        // catalog entry is not needed, verify when it's needed
                        .build()
                )
                .dataDestination(DataAddress.Builder.newInstance()
                        .type("parts-tree") //the provider uses this to select the correct DataFlowController
                        .property("destinationPath", destinationPath) //where we want the file to be stored
                        .property("view", view)
                        .property("vin", vin)
                        .property("path", "PARTS_TREE_BY_VIN")
                        .build())
                .managedResources(false) //we do not need any provisioning
                .build();

        var response = processManager.initiateConsumerRequest(dataRequest);
        return response.getStatus() != ResponseStatus.OK ? Response.status(400).build() : Response.ok(response.getId()).build();

    }
}
