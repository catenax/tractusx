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
import net.catenax.prs.client.model.PartRelationship;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.constants.PrsConnectorConstants;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.consumer.registry.StubRegistryClient;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.job.RecursiveJobHandler;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
     * The name of the blob to be created in each Provider call.
     * The suffix ".complete" is required in order to signal to the
     * EDC ObjectContainerStatusChecker that a transfer is complete.
     * The checker lists blobs on the destination container until a blob with this suffix
     * in the name is present.
     */
    /* package */ static final String BLOB_NAME = "partialPartsTree.complete";
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
     * Registry client to resolve Provider URL by Part ID.
     */
    private final StubRegistryClient registryClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> initiate(final MultiTransferJob job) {
        monitor.info("Initiating recursive retrieval for Job " + job.getJobId());
        final FileRequest fileRequest = getFileRequest(job);
        PartId partId = toPartId(fileRequest.getPartsTreeRequest());
        final var request = xgetDataRequest(fileRequest, partId);
        return request.stream();
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

        return tree.getRelationships().stream()
                .map(PartRelationship::getChild)
                .filter(p -> !previousUrl.equals(registryClient.getUrl(p).orElse(null)))
                .flatMap(p -> xgetDataRequest(previousRequest, p).stream());
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
        final byte[] blob;
        if (completedTransfers.isEmpty()) {
            monitor.info("No partial parts trees, creating empty parts tree");
            final var result = new PartRelationshipsWithInfos();
            blob = jsonUtil.asString(result).getBytes(StandardCharsets.UTF_8);
        } else {
            final var firstTransfer = completedTransfers.get(0);
            blob = downloadPartialPartsTree(firstTransfer);
        }
        return blob;
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

    private Optional<DataRequest> xgetDataRequest(FileRequest fileRequest, PartId partId) {
        var newPartsTreeRequest = fileRequest.getPartsTreeRequest().toBuilder()
                .oneIDManufacturer(partId.getOneIDManufacturer())
                .objectIDManufacturer(partId.getObjectIDManufacturer())
                .build();

        final var partsTreeRequestAsString = jsonUtil.asString(newPartsTreeRequest);

        final var addr = registryClient.getUrl(partId);
        monitor.info("Mapped data request to " + addr);

        return addr.map(url -> DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(url) //the address of the provider connector
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
                        PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH, BLOB_NAME
                ))
                .managedResources(true)
                .build());
    }

    private PartId toPartId(PartsTreeByObjectIdRequest partsTreeRequest) {
        var partId = new PartId();
        partId.setOneIDManufacturer(partsTreeRequest.getOneIDManufacturer());
        partId.setObjectIDManufacturer(partsTreeRequest.getObjectIDManufacturer());
        return partId;
    }

    private FileRequest getFileRequest(final MultiTransferJob job) {
        final var fileRequestAsString = job.getJobData().get(ConsumerService.PARTS_REQUEST_KEY);
        return jsonUtil.fromString(fileRequestAsString, FileRequest.class);
    }

}
