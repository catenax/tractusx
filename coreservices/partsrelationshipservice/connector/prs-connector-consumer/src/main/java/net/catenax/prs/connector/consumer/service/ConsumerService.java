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
import net.catenax.prs.connector.job.JobInitiateResponse;
import net.catenax.prs.connector.job.JobOrchestrator;
import net.catenax.prs.connector.job.JobState;
import net.catenax.prs.connector.job.JobStore;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.requests.FileRequest;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.util.Map;
import java.util.Optional;

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

        final var response = jobOrchestrator.startJob(Map.of(
                PARTS_REQUEST_KEY, serializedRequest
        ));
        return Optional.of(response);
    }

    /**
     * Provides status of a job
     *
     * @param jobId If of the job
     * @return Job state
     */
    public Optional<JobState> getStatus(final String jobId) {
        monitor.info("Getting status of job " + jobId);

        return jobStore.find(jobId)
                .map(MultiTransferJob::getState);
    }
}
