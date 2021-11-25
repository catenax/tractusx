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
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.constants.PrsConnectorConstants;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * XXX.
 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class PartsTreeRecursiveLogic {

    /**
     * Logger.
     */
    private final Monitor monitor;
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
     * XXX
     */
    private final PartsTreesAssembler assembler;

    /**
     * XXX.
     *
     * @param fileRequest XXX.
     * @return XXX.
     */
    /* package */ Stream<DataRequest> initiate(final FileRequest fileRequest) {
        final var partId = toPartId(fileRequest.getPartsTreeRequest());
        return dataRequestGenerator.generateRequest(fileRequest, partId).stream();
    }

    /**
     * XXX.
     *
     * @param transferProcess XXX.
     * @param requestTemplate XXX.
     * @return XXX.
     */
    /* package */ Stream<DataRequest> recurse(final TransferProcess transferProcess, final FileRequest requestTemplate) {
        return Stream.of();
    }

    /**
     * XXX.
     *
     * @param completedTransfers  XXX.
     * @param targetAccountName   XXX.
     * @param targetContainerName XXX.
     * @param targetBlobName      XXX.
     */
    /* package */ void complete(
            final List<TransferProcess> completedTransfers,
            final String targetAccountName,
            final String targetContainerName,
            final String targetBlobName) {
        final var partialTrees = completedTransfers.stream()
                .map(this::downloadPartialPartsTree)
                .map(payload -> jsonUtil.fromString(new String(payload), PartRelationshipsWithInfos.class));
        final var assembledTree = assembler.assemblePartsTrees(partialTrees);
        final var blob = jsonUtil.asString(assembledTree).getBytes(StandardCharsets.UTF_8);

        monitor.info(format("Uploading assembled parts tree to %s/%s/%s",
                targetAccountName,
                targetContainerName,
                targetBlobName
        ));
        blobStoreApi.putBlob(targetAccountName, targetContainerName, targetBlobName, blob);
    }

    private byte[] downloadPartialPartsTree(final TransferProcess transfer) {
        final var destination = transfer.getDataRequest().getDataDestination();
        final var sourceAccountName = destination.getProperty(AzureBlobStoreSchema.ACCOUNT_NAME);
        final var sourceContainerName = destination.getProperty(AzureBlobStoreSchema.CONTAINER_NAME);
        final var sourceBlobName = transfer.getDataRequest().getProperties().get(PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH);
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
}
