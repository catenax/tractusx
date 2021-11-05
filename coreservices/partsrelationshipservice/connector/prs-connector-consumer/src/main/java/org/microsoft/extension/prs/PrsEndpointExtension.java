package org.microsoft.extension.prs;

import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;

import java.util.Set;

public class PrsEndpointExtension implements ServiceExtension {
    @Override
    public Set<String> requires() {
        return Set.of("edc:webservice");
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var webService = context.getService(WebService.class);
        var processManager = context.getService(TransferProcessManager.class);
        webService.registerController(new PrsApiController(context.getMonitor(), processManager));
    }
}