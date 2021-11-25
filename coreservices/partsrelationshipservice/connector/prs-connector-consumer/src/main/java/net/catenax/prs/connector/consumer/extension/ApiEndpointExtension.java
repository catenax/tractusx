//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.extension;


import jakarta.validation.Validation;
import net.catenax.prs.connector.annotations.ExcludeFromCodeCoverageGeneratedReport;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.consumer.configuration.PartitionDeploymentsConfig;
import net.catenax.prs.connector.consumer.configuration.PartitionsConfig;
import net.catenax.prs.connector.consumer.controller.ConsumerApiController;
import net.catenax.prs.connector.consumer.middleware.RequestMiddleware;
import net.catenax.prs.connector.consumer.registry.StubRegistryClient;
import net.catenax.prs.connector.consumer.service.ConsumerService;
import net.catenax.prs.connector.consumer.service.DataRequestGenerator;
import net.catenax.prs.connector.consumer.service.PartsTreeRecursiveJobHandler;
import net.catenax.prs.connector.job.InMemoryJobStore;
import net.catenax.prs.connector.job.JobOrchestrator;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessObservable;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * Extension providing extra consumer endpoints.
 */
@ExcludeFromCodeCoverageGeneratedReport
public class ApiEndpointExtension implements ServiceExtension {

    /**
     * The configuration property used to reference the storage account name
     * for connector data exchange.
     */
    public static final String EDC_STORAGE_ACCOUNT_NAME = "edc.storage.account.name";

    /***
     * The configuration property used to reference
     * the {@literal cd/dataspace-partitions.json} configuration file.
     * */
    public static final String DATASPACE_PARTITIONS = "prs.dataspace.partitions";

    /**
     * The configuration property used to reference
     * the {@literal dataspace-deployments.json} file
     * generated from Terraform outputs in CD pipeline.
     */
    public static final String DATASPACE_PARTITION_DEPLOYMENTS = "prs.dataspace.partition.deployments";

    private static <T> T readJson(
            final ServiceExtensionContext context,
            final JsonUtil jsonUtil,
            final String property,
            final String defaultValue,
            final Class<T> type,
            final String message) {
        final var path = ofNullable(context.getSetting(property, defaultValue))
                .orElseThrow(() -> new EdcException("Missing property " + property));
        try {
            final var json = Files.readString(Paths.get(path));
            return jsonUtil.fromString(json, type);
        } catch (IOException | EdcException e) {
            throw new EdcException("Couldn't parse " + path + ". " + message, e);
        }
    }

    @Override
    public Set<String> requires() {
        return Set.of(
                "edc:webservice",
                "dataspaceconnector:transferprocessstore",
                "dataspaceconnector:blobstoreapi"
        );
    }

    @Override
    public void initialize(final ServiceExtensionContext context) {
        final var storageAccountName = ofNullable(context.getSetting(EDC_STORAGE_ACCOUNT_NAME, null))
                .orElseThrow(() -> new EdcException("Missing mandatory property " + EDC_STORAGE_ACCOUNT_NAME));

        final var monitor = context.getMonitor();
        final var jsonUtil = new JsonUtil(monitor);

        final var partitionsConfig = readJson(
                context,
                jsonUtil,
                DATASPACE_PARTITIONS,
                "../../cd/dataspace-partitions.json",
                PartitionsConfig.class,
                "");
        final var partitionDeploymentsConfig = readJson(
                context,
                jsonUtil,
                DATASPACE_PARTITION_DEPLOYMENTS,
                "../dataspace-deployments.json",
                PartitionDeploymentsConfig.class,
                "For development, see README.md for instructions on downloading the file.");

        final var validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();

        final var middleware = new RequestMiddleware(monitor, validator);
        final var webService = context.getService(WebService.class);
        final var processManager = context.getService(TransferProcessManager.class);
        final var transferProcessObservable = context.getService(TransferProcessObservable.class);

        final var blobStoreApi = context.getService(BlobStoreApi.class);
        final var jobStore = new InMemoryJobStore(monitor);
        final var configuration = ConsumerConfiguration.builder().storageAccountName(storageAccountName).build();
        final var registryClient = new StubRegistryClient(partitionsConfig, partitionDeploymentsConfig);
        final var dataRequestGenerator = new DataRequestGenerator(monitor, configuration, jsonUtil, registryClient);
        final var jobHandler = new PartsTreeRecursiveJobHandler(monitor, configuration, blobStoreApi, jsonUtil, dataRequestGenerator);
        final var jobOrchestrator = new JobOrchestrator(processManager, jobStore, jobHandler, transferProcessObservable, monitor);

        final var service = new ConsumerService(monitor, jsonUtil, jobStore, jobOrchestrator, blobStoreApi, configuration);

        webService.registerController(new ConsumerApiController(monitor, service, middleware));
    }
}
