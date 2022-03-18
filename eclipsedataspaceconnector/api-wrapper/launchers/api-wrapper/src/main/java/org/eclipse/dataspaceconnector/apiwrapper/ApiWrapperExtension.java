package org.eclipse.dataspaceconnector.apiwrapper;

import okhttp3.OkHttpClient;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractOfferService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractNegotiationService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.HttpProxyService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.TransferProcessService;
import org.eclipse.dataspaceconnector.spi.WebService;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.Inject;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;

public class ApiWrapperExtension implements ServiceExtension {

    @Inject
    private WebService webService;

    @Inject
    private OkHttpClient httpClient;

    @Override
    public String name() {
        return "AAS-API-Wrapper";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        Monitor monitor = context.getMonitor();
        var typeManager = context.getTypeManager();

        var contractOfferService = new ContractOfferService(monitor, typeManager, httpClient);
        var contractOfferRequestService = new ContractNegotiationService(monitor, typeManager, httpClient);
        var transferProcessService = new TransferProcessService(monitor, typeManager, httpClient);
        var httpProxyService = new HttpProxyService(monitor, httpClient);

        webService.registerResource(new ApiWrapperController(
                monitor,
                contractOfferService,
                contractOfferRequestService,
                transferProcessService,
                httpProxyService
        ));
    }
}
