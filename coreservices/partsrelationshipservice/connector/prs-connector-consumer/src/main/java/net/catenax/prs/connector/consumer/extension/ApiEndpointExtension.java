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

import net.catenax.prs.connector.annotations.ExcludeFromCodeCoverageGeneratedReport;
import net.catenax.prs.connector.consumer.controller.ConsumerApiController;
import net.catenax.prs.connector.consumer.middleware.RequestMiddleware;
import net.catenax.prs.connector.consumer.service.ConsumerService;
import net.catenax.prs.connector.consumer.transfer.FileStatusChecker;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusCheckerRegistry;

import java.util.Set;

/**
 * Extension providing extra consumer endpoints.
 */
@ExcludeFromCodeCoverageGeneratedReport
public class ApiEndpointExtension implements ServiceExtension {

    @Override
    public Set<String> requires() {
        return Set.of(
                "edc:webservice",
                "dataspaceconnector:transferprocessstore"
        );
    }

    @Override
    public void initialize(final ServiceExtensionContext context) {
        final var monitor = context.getMonitor();

        final var middleware = new RequestMiddleware(monitor);

        final var webService = context.getService(WebService.class);
        final var processManager = context.getService(TransferProcessManager.class);
        final var processStore = context.getService(TransferProcessStore.class);
        final var service = new ConsumerService(monitor, processManager, processStore);

        webService.registerController(new ConsumerApiController(monitor, service, middleware));

        final var statusCheckerReg = context.getService(StatusCheckerRegistry.class);
        statusCheckerReg.register("File", new FileStatusChecker(monitor));
    }
}
