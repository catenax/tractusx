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
import net.catenax.prs.connector.constants.PrsConnectorConstants;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.consumer.registry.StubRegistryClient;
import net.catenax.prs.connector.job.RecursiveJobHandler;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * XXX

 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class DataRequestGenerator {

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
     * Json Converter.
     */
    private final JsonUtil jsonUtil;
    /**
     * Registry client to resolve Provider URL by Part ID.
     */
    private final StubRegistryClient registryClient;

    /* package */ Stream<DataRequest> generateRequests(FileRequest previousRequest, String previousUrl, Stream<PartId> partIdStream) {
        return partIdStream
                .filter(p -> !Objects.equals(previousUrl, registryClient.getUrl(p).orElse(null)))
                .flatMap(p -> generateRequest(previousRequest, p).stream());
    }

    private Optional<DataRequest> generateRequest(FileRequest fileRequest, PartId partId) {
        final var newPartsTreeRequest = fileRequest.getPartsTreeRequest().toBuilder()
                .oneIDManufacturer(partId.getOneIDManufacturer())
                .objectIDManufacturer(partId.getObjectIDManufacturer())
                .build();

        final var partsTreeRequestAsString = jsonUtil.asString(newPartsTreeRequest);

        final var connectorAddress = registryClient.getUrl(partId);
        monitor.info("Mapped data request to " + connectorAddress);

        return connectorAddress.map(url -> DataRequest.Builder.newInstance()
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
}
