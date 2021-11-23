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
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.constants.PrsConnectorConstants;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.job.RecursiveJobHandler;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.String.format;

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
     * Logger.
     */
    private final Monitor monitor;
    /**
     * Storage account name.
     */
    private final ConsumerConfiguration configuration;
    /**
     * Blob store API.
     */
    private final BlobStoreApi blobStoreApi;
    /**
     * Json Converter.
     */
    private final JsonUtil jsonUtil;

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> initiate(final MultiTransferJob job) {
        monitor.info("Initiating recursive retrieval for Job " + job.getJobId());
        final var request = dataRequest(job);
        return Stream.of(request);
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
        final var completedTransfers = job.getCompletedTransfers();
        final var accountName2 = configuration.getStorageAccountName();
        final var containerName2 = job.getJobData().get(ConsumerService.CONTAINER_NAME_KEY);
        final var blobName2 = job.getJobData().get(ConsumerService.DESTINATION_PATH_KEY);
        if (completedTransfers.isEmpty()) {
            final var result = new PartRelationshipsWithInfos();
            final byte[] blob = jsonUtil.asString(result).getBytes(StandardCharsets.UTF_8);
            blobStoreApi.putBlob(accountName2, containerName2, blobName2, blob);
        } else {
            final var firstTransfer = completedTransfers.get(0);
            final var destination = firstTransfer.getDataRequest().getDataDestination();
            copyBlob(destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME),
                    destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME),
                    firstTransfer.getDataRequest().getProperties().get(PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH),
                    accountName2,
                    containerName2,
                    blobName2);
        }
    }

    private DataRequest dataRequest(final MultiTransferJob job) {
        final var fileRequestAsString = job.getJobData().get(ConsumerService.PARTS_REQUEST_KEY);
        final var fileRequest = jsonUtil.fromString(fileRequestAsString, FileRequest.class);
        final var partsTreeRequestAsString = jsonUtil.asString(fileRequest.getPartsTreeRequest());

        final var destinationPath = job.getJobData().get(ConsumerService.DESTINATION_PATH_KEY);

        return DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(fileRequest.getConnectorAddress()) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id(PrsConnectorConstants.PRS_REQUEST_ASSET_ID)
                        .policyId(PrsConnectorConstants.PRS_REQUEST_POLICY_ID)
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE) //the provider uses this to select the correct DataFlowController
                        .property(AzureBlobStoreSchema.ACCOUNT_NAME, configuration.getStorageAccountName())
                        .build())
                .properties(Map.of(
                        PrsConnectorConstants.DATA_REQUEST_PRS_REQUEST_PARAMETERS, partsTreeRequestAsString,
                        PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH, destinationPath
                ))
                .managedResources(true)
                .build();
    }

    private void copyBlob(
            final String accountName1, final String containerName1, final String blobName1,
            final String accountName2, final String containerName2, final String blobName2) {
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
