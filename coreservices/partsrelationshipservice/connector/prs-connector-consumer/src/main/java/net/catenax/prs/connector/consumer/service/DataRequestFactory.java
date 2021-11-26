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
 * Generates EDC {@link DataRequest}s populated for calling Providers to invoke the PRS API
 * to retrieve partial parts trees.
 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class DataRequestFactory {

    /**
     * The name of the blob to be created in each Provider call.
     * The suffix ".complete" is required in order to signal to the
     * EDC ObjectContainerStatusChecker that a transfer is complete.
     * The checker lists blobs on the destination container until a blob with this suffix
     * in the name is present.
     */
    /* package */ static final String PARTIAL_PARTS_TREE_BLOB_NAME = "partialPartsTree.complete";
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

    /**
     * Generates EDC {@link DataRequest}s populated for calling Providers to invoke the PRS API
     * to retrieve partial parts trees.
     * <p>
     * If the {@code previousUrlOrNull} argument is non-{@code null}, this method will not return
     * data requests pointing to that Provider URL. This ensures only parts tree queries pointing
     * to other providers are issued in subsequent recursive retrievals.
     *
     * @param requestTemplate   client request.
     * @param previousUrlOrNull the Provider URL used for retrieving the {@code partIds},
     *                          or {@code null} for the first retrieval.
     * @param partIds           the parts for which to retrieve partial parts trees.
     * @return a {@link DataRequest} for each item {@code partIds} for which the Provider URL
     * was resolves in the registry <b>and</b> is not identical to {@code previousUrlOrNull},
     * that allows retrieving the partial parts tree for the given part.
     */
    /* package */ Stream<DataRequest> createRequests(
            final FileRequest requestTemplate,
            final String previousUrlOrNull,
            final Stream<PartId> partIds) {
        return partIds
                .filter(p -> !Objects.equals(previousUrlOrNull, registryClient.getUrl(p).orElse(null)))
                .flatMap(p -> createRequest(requestTemplate, p).stream());
    }

    private Optional<DataRequest> createRequest(final FileRequest requestTemplate, final PartId partId) {
        final var newPartsTreeRequest = requestTemplate.getPartsTreeRequest().toBuilder()
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
                        PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH, PARTIAL_PARTS_TREE_BLOB_NAME
                ))
                .managedResources(true)
                .build());
    }
}
