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
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.job.RecursiveJobHandler;
import net.catenax.prs.connector.requests.FileRequest;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.String.format;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.PRS_REQUEST_ASSET_ID;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_REQUEST_PARAMETERS;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.PRS_REQUEST_POLICY_ID;

/**
 * Implementation of {@link RecursiveJobHandler} that retrieves
 * the parts tree.
 * <p>
 * In this increment, the implementation only retrieves the first level
 * parts tree, as a non-recursive implementation would do. In a next
 * increment, this class will be extended to perform recursive queries
 * by querying multiple PRS API instances.
 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class PartsTreeRecursiveJobHandler implements RecursiveJobHandler {

    /**
     * JSON object mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * Logger.
     */
    private final Monitor monitor;
    /**
     * Storage account name.
     */
    private final ConsumerConfiguration configuration;
    /**
     * Blob store API
     */
    private final BlobStoreApi blobStoreApi;

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> initiate(final MultiTransferJob job) {
        monitor.info("Initiating recursive retrieval for Job " + job.getJobId());
        final var request = dataRequest(job);
        return request.isPresent() ? Stream.of(request.get()) : Stream.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> recurse(final MultiTransferJob job, final TransferProcess transferProcess) {
        monitor.info("Proceeding with recursive retrieval for Job " + job.getJobId());
        return Stream.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void complete(final MultiTransferJob job) {
        monitor.info("Completed retrieval for Job " + job.getJobId());
        final var firstTransfer = job.getCompletedTransfers().get(0);
        final var destination = firstTransfer.getDataRequest().getDataDestination();
        copyBlob(destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME),
                destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME),
                firstTransfer.getDataRequest().getProperties().get(DATA_REQUEST_PRS_DESTINATION_PATH),
                configuration.getStorageAccountName(),
                job.getJobData().get(ConsumerService.CONTAINER_NAME_KEY),
                job.getJobData().get(ConsumerService.DESTINATION_PATH_KEY));
    }

    private Optional<DataRequest> dataRequest(final MultiTransferJob job) {
        final var fileRequestAsString = job.getJobData().get(ConsumerService.PARTS_REQUEST_KEY);
        final FileRequest fileRequest;
        try {
            fileRequest = MAPPER.readValue(fileRequestAsString, FileRequest.class);
        } catch (JsonProcessingException e) {
            monitor.severe("Error deserializing request", e);
            return Optional.empty();
        }

        String partsTreeRequestAsString;
        try {
            partsTreeRequestAsString = MAPPER.writeValueAsString(fileRequest.getPartsTreeRequest());
        } catch (JsonProcessingException e) {
            monitor.severe("Error serializing request", e);
            return Optional.empty();
        }

        return Optional.of(DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(fileRequest.getConnectorAddress()) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id(PRS_REQUEST_ASSET_ID)
                        .policyId(PRS_REQUEST_POLICY_ID)
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE) //the provider uses this to select the correct DataFlowController
                        .property(AzureBlobStoreSchema.ACCOUNT_NAME, configuration.getStorageAccountName())
                        .build())
                .properties(Map.of(
                        DATA_REQUEST_PRS_REQUEST_PARAMETERS, partsTreeRequestAsString,
                        DATA_REQUEST_PRS_DESTINATION_PATH, fileRequest.getDestinationPath()
                ))
                .managedResources(true)
                .build());
    }

    private void copyBlob(
            String accountName1, String containerName1, String blobName1,
            String accountName2, String containerName2, String blobName2) {
        monitor.info(format("Copying blob from %s/%s/%s to %s/%s/%s",
                accountName1,
                containerName1,
                blobName1,
                accountName2,
                containerName2,
                blobName2
        ));
        final var blob = blobStoreApi.getBlob(accountName1, containerName1, blobName1);
        blobStoreApi.putBlob(accountName2, containerName2, blobName2, blob);
    }
}
