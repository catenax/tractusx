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
import net.catenax.prs.connector.consumer.registry.StubRegistryClient;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.job.RecursiveJobHandler;
import net.catenax.prs.connector.requests.FileRequest;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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

    private final StubRegistryClient registryClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> initiate(final MultiTransferJob job) {
        monitor.info("Initiating recursive retrieval for Job " + job.getJobId());
        final var request = dataRequest(job);
        return request.stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<DataRequest> recurse(final MultiTransferJob job, final TransferProcess transferProcess) {
        monitor.info("Proceeding with recursive retrieval for Job " + job.getJobId());

        final var previousRequest = getFileRequest(job);
        final var newRequest =
                previousRequest.toBuilder().partsTreeRequest(
                        previousRequest.getPartsTreeRequest().toBuilder()
                                .oneIDManufacturer("CAXLBRHHQAJAIOZZ") // ZF
                                .build()
                ).build();
        var previousUrl = transferProcess.getDataRequest().getConnectorAddress();
        var newUrl = registryClient.getUrl(newRequest.getPartsTreeRequest());
        if (newUrl.isPresent() && !newUrl.get().equals(previousUrl)) {
            var r = dataRequest(newRequest);
            return r.stream();
        }
        return Stream.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void complete(final MultiTransferJob job) {
        monitor.info("Completed with recursive retrieval for Job " + job.getJobId());
    }

    private Optional<DataRequest> dataRequest(final MultiTransferJob job) {
        final var fileRequest = getFileRequest(job);
        return dataRequest(fileRequest);
    }

    private FileRequest getFileRequest(MultiTransferJob job) {
        final var fileRequestAsString = job.getJobData().get(ConsumerService.PARTS_REQUEST_KEY);
        try {
            return MAPPER.readValue(fileRequestAsString, FileRequest.class);
        } catch (JsonProcessingException e) {
            monitor.severe("Error deserializing request", e);
            throw new EdcException(e);
        }
    }

    @NotNull
    private Optional<DataRequest> dataRequest(FileRequest fileRequest) {
        String partsTreeRequestAsString;
        try {
            partsTreeRequestAsString = MAPPER.writeValueAsString(fileRequest.getPartsTreeRequest());
        } catch (JsonProcessingException e) {
            monitor.severe("Error serializing request", e);
            return Optional.empty();
        }

        var addr = registryClient.getUrl(fileRequest.getPartsTreeRequest());
        monitor.info("Mapped data request to " + addr);

        return addr.map(url -> DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(url) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id("prs-request")
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE) //the provider uses this to select the correct DataFlowController
                        .property("account", configuration.getStorageAccountName())
                        .build())
                .properties(Map.of(
                        "prs-request-parameters", partsTreeRequestAsString,
                        "prs-destination-path", fileRequest.getDestinationPath()
                ))
                .managedResources(true)
                .build());
    }
}
