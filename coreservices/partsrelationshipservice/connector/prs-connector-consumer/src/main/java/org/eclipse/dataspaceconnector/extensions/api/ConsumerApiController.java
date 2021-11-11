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
import org.eclipse.dataspaceconnector.extensions.job.InMemoryJobStore;
import org.eclipse.dataspaceconnector.extensions.job.JobInitiateResponse;
import org.eclipse.dataspaceconnector.extensions.job.JobOrchestrator;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;

import java.util.Objects;

import static java.lang.String.format;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/")
public class ConsumerApiController {

    private final Monitor monitor;
    private final TransferProcessManager processManager;
    private final TransferProcessStore processStore;
    private final JobOrchestrator jobOrchestrator;

    public ConsumerApiController(Monitor monitor, TransferProcessManager processManager, TransferProcessStore processStore, JobOrchestrator jobOrchestrator) {
        this.monitor = monitor;
        this.processManager = processManager;
        this.processStore = processStore;
        this.jobOrchestrator = jobOrchestrator;
    }

    @GET
    @Path("health")
    public String checkHealth() {
        monitor.info("%s :: Received a health request");
        return "I'm alive!";
    }

    @POST
    @Path("file/{filename}")
    public Response initiateTransfer(@PathParam("filename") String filename, @QueryParam("connectorAddress") String connectorAddress,
                                     @QueryParam("destination") String destinationPath) {

        monitor.info(format("Received request for file %s against provider %s", filename, connectorAddress));

        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(connectorAddress, "connectorAddress");

        JobInitiateResponse response = jobOrchestrator.startJob(filename, connectorAddress, destinationPath);

        return response.getStatus() != ResponseStatus.OK ? Response.status(400).build() : Response.ok(response.getId()).build();
    }

    @GET
    @Path("job/{id}/state")
    public Response getJobStatus(@PathParam("id") String jobId) {
        monitor.info("Getting status of job " + jobId);

        var job = jobOrchestrator.findJob(jobId);
        if (job == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(job.getState().toString()).build();
    }

    @GET
    @Path("process/{id}/state")
    public Response getTransferProcessStatus(@PathParam("id") String requestId) {
        monitor.info("Getting status of data request " + requestId);

        var process = processStore.find(requestId);
        if (process == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(TransferProcessStates.from(process.getState()).toString()).build();
    }
}
