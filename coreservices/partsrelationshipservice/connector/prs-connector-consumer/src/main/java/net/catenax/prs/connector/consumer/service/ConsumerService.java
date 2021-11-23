//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.job.JobInitiateResponse;
import net.catenax.prs.connector.job.JobOrchestrator;
import net.catenax.prs.connector.job.JobState;
import net.catenax.prs.connector.job.JobStore;
import net.catenax.prs.connector.requests.FileRequest;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

/**
 * Consumer Service.
 * Provides job management.
 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class ConsumerService {
    /**
     * Key for the serialized request stored in the Job Data.
     */
    /* package */ static final String PARTS_REQUEST_KEY = "ser-request";
    /**
     * XXX.
     */
    /* package */ static final String CONTAINER_NAME_KEY = "blob-container-name";
    /**
     * XXX.
     */
    /* package */ static final String DESTINATION_PATH_KEY = "blob-destination-path";
    /**
     * JSON object mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * Logger.
     */
    private final Monitor monitor;
    /**
     * Job Orchestrator.
     */
    private final JobStore jobStore;
    /**
     * Job Orchestrator.
     */
    private final JobOrchestrator jobOrchestrator;
    /**
     * Blob store API
     */
    private final BlobStoreApi blobStoreApi;
    /**
     * Consumer configuration
     */
    private final ConsumerConfiguration consumerConfiguration;

    /**
     * Endpoint to trigger a request, so that a file get copied into a specific destination.
     *
     * @param request Request parameters.
     * @return TransferInitiateResponse with process id.
     */
    public Optional<JobInitiateResponse> initiateTransfer(final FileRequest request) {
        monitor.info(format("Received request against provider %s", request.getConnectorAddress()));

        final String serializedRequest;
        try {
            serializedRequest = MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            // should not happen
            monitor.severe("Error serializing request", e);
            return Optional.empty();
        }

        final var storageAccountName = consumerConfiguration.getStorageAccountName();
        String containerName = UUID.randomUUID().toString();
        blobStoreApi.createContainer(storageAccountName, containerName);

        final var response = jobOrchestrator.startJob(
                Map.of(
                        PARTS_REQUEST_KEY, serializedRequest,
                        CONTAINER_NAME_KEY, containerName,
                        DESTINATION_PATH_KEY, request.getDestinationPath()
                        )
        );
        return Optional.of(response);
    }

    /**
     * Provides status of a job
     *
     * @param jobId If of the job
     * @return Job state
     */
    public Optional<StatusResponse> getStatus(final String jobId) {
        monitor.info("Getting status of job " + jobId);

        return jobStore.find(jobId).map(job -> {
            monitor.info("Status of job " + jobId + ":" + job.getState());
            final var response = StatusResponse.builder().status(job.getState());
            if (job.getState() == JobState.COMPLETED) {
                response.sasToken(createSasUrl(job.getJobData()).toString());
            }
            return response.build();
        });
    }

    private URL createSasUrl(final Map<String, String> jobData) {
        final var storageAccountName = consumerConfiguration.getStorageAccountName();
        final var containerName = jobData.get(CONTAINER_NAME_KEY);
        final var destinationPath = jobData.get(DESTINATION_PATH_KEY);

        final var sasToken = blobStoreApi.createContainerSasToken(storageAccountName, containerName, "r", OffsetDateTime.now().plusHours(1));

        try {
            return new URL("https://" + storageAccountName + ".blob.core.windows.net/" + containerName + "/" + destinationPath + "?" + sasToken);
        } catch (MalformedURLException e) {
            throw new EdcException("Invalid url", e);
        }
    }
}
