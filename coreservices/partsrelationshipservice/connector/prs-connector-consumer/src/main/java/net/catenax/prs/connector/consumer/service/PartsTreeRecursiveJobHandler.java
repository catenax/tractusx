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
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartInfo;
import net.catenax.prs.client.model.PartRelationship;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.constants.PrsConnectorConstants;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.job.RecursiveJobHandler;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
     * XXX
     */
    private final DataRequestGenerator dataRequestGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> initiate(final MultiTransferJob job) {
        monitor.info("Initiating recursive retrieval for Job " + job.getJobId());
        final FileRequest fileRequest = getFileRequest(job);
        PartId partId = toPartId(fileRequest.getPartsTreeRequest());
        return dataRequestGenerator.generateRequests(fileRequest, null, Stream.of(partId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> recurse(final MultiTransferJob job, final TransferProcess transferProcess) {
        monitor.info("Proceeding with recursive retrieval for Job " + job.getJobId());

        final var previousRequest = getFileRequest(job);

        final var previousUrl = transferProcess.getDataRequest().getConnectorAddress();
        final var blob = downloadPartialPartsTree(transferProcess);
        final var tree = jsonUtil.fromString(new String(blob), PartRelationshipsWithInfos.class);

        Stream<PartId> partIdStream = tree.getRelationships().stream()
                .map(PartRelationship::getChild);
        return dataRequestGenerator.generateRequests(previousRequest, previousUrl, partIdStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void complete(final MultiTransferJob job) {
        monitor.info("Completed retrieval for Job " + job.getJobId());
        final var completedTransfers = job.getCompletedTransfers();
        final var targetAccountName = configuration.getStorageAccountName();
        final var targetContainerName = job.getJobData().get(ConsumerService.CONTAINER_NAME_KEY);
        final var targetBlobName = job.getJobData().get(ConsumerService.DESTINATION_PATH_KEY);

        final byte[] blob = assemblePartsTrees(completedTransfers);

        monitor.info(format("Uploading assembled parts tree to %s/%s/%s",
                targetAccountName,
                targetContainerName,
                targetBlobName
        ));
        blobStoreApi.putBlob(targetAccountName, targetContainerName, targetBlobName, blob);
    }

    private byte[] assemblePartsTrees(List<TransferProcess> completedTransfers) {
        monitor.info(format("Assembling parts tree from %d partial parts trees", completedTransfers.size()));

        final var relationships = new LinkedHashSet<PartRelationship>();
        final var partInfos = new LinkedHashSet<PartInfo>();
        for (var transfer: completedTransfers) {
            final var blob = downloadPartialPartsTree(transfer);
            final var partialTree = jsonUtil.fromString(new String(blob), PartRelationshipsWithInfos.class);
            relationships.addAll(partialTree.getRelationships());
            partInfos.addAll(partialTree.getPartInfos());
        }
        final var result = new PartRelationshipsWithInfos();
        result.setRelationships(new ArrayList<>(relationships));
        result.setPartInfos(new ArrayList<>(partInfos));
        return jsonUtil.asString(result).getBytes(StandardCharsets.UTF_8);
    }

    private byte[] downloadPartialPartsTree(TransferProcess firstTransfer) {
        final var destination = firstTransfer.getDataRequest().getDataDestination();
        final var sourceAccountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        final var sourceContainerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        final var sourceBlobName = firstTransfer.getDataRequest().getProperties().get(PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH);
        monitor.info(format("Downloading partial parts tree from blob at %s/%s/%s",
                sourceAccountName,
                sourceContainerName,
                sourceBlobName
        ));
        return blobStoreApi.getBlob(sourceAccountName, sourceContainerName, sourceBlobName);
    }

    private PartId toPartId(final PartsTreeByObjectIdRequest partsTreeRequest) {
        final var partId = new PartId();
        partId.setOneIDManufacturer(partsTreeRequest.getOneIDManufacturer());
        partId.setObjectIDManufacturer(partsTreeRequest.getObjectIDManufacturer());
        return partId;
    }

    private FileRequest getFileRequest(final MultiTransferJob job) {
        final var fileRequestAsString = job.getJobData().get(ConsumerService.PARTS_REQUEST_KEY);
        return jsonUtil.fromString(fileRequestAsString, FileRequest.class);
    }

}
