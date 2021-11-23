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


import lombok.RequiredArgsConstructor;
import net.catenax.prs.connector.job.JobInitiateResponse;
import net.catenax.prs.connector.job.JobOrchestrator;
import net.catenax.prs.connector.job.JobStore;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.util.JsonUtil;
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
     * Logger.
     */
    private final Monitor monitor;
    /**
     * JSON object mapper.
     */
    private final JsonUtil jsonUtil;
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
    public JobInitiateResponse initiateTransfer(final FileRequest request) {
        monitor.info(format("Received request against provider %s", request.getConnectorAddress()));

        final String serializedRequest = jsonUtil.asString(request);

        return jobOrchestrator.startJob(Map.of(
                PARTS_REQUEST_KEY, serializedRequest
        ));
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
            return response.build();
        });
    }
}
