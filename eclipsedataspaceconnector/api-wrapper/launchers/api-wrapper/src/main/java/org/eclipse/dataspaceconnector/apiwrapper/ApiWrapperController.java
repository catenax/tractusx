package org.eclipse.dataspaceconnector.apiwrapper;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractNegotiationService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.ContractOfferService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.HttpProxyService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service.TransferProcessService;
import org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.model.NegotiationStatusResponse;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.negotiation.ContractOfferRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.edr.EndpointDataReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Consumes()
@Produces({MediaType.APPLICATION_JSON})
@Path("/service")
public class ApiWrapperController {

    private final Monitor monitor;
    private final ContractOfferService contractOfferService;
    private final ContractNegotiationService contractNegotiationService;
    private final TransferProcessService transferProcessService;
    private final HttpProxyService httpProxyService;

    private final Map<String, String> header = Map.of("X-Api-Key", "123456");

    private final String consumerControlPlaneBaseUrl = "http://consumer-control-plane:9191/api";
    private final String providerControlPlaneFormat = "%s/api/v1/ids/data";

    private final static Pattern RESPONSE_PATTERN = Pattern.compile("\\{\"data\":\"(?<embeddedData>.*)\"\\}");

    private final Map<String, EndpointDataReference> endpointDataReferences = new HashMap<>();

    public ApiWrapperController(Monitor monitor,
                                ContractOfferService contractOfferService,
                                ContractNegotiationService contractNegotiationService,
                                TransferProcessService transferProcessService, HttpProxyService httpProxyService) {
        this.monitor = monitor;
        this.contractOfferService = contractOfferService;
        this.contractNegotiationService = contractNegotiationService;
        this.transferProcessService = transferProcessService;
        this.httpProxyService = httpProxyService;
    }

    @GET
    @Path("/{assetId}/{subUrl:.+}")
    public String getWrapper(@QueryParam("provider-connector-url") String providerConnectorUrl, @PathParam("assetId") String assetId, @PathParam("subUrl") String subUrl, @Context UriInfo uriInfo) throws InterruptedException {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        String providerControlPlaneIDSUrl = String.format(providerControlPlaneFormat,providerConnectorUrl);

        // Initialize and negotiate everything
        // TODO do this only if no agreement is already existing
        var agreementId = initializeContractNegotiation(providerControlPlaneIDSUrl,assetId);

        // Initiate transfer process
        transferProcessService.initiateHttpProxyTransferProcess(
                agreementId,
                assetId,
                consumerControlPlaneBaseUrl,
                providerControlPlaneIDSUrl,
                header
        );

        EndpointDataReference dataReference = null;
        while (dataReference == null) {
            Thread.sleep(1000);
            dataReference = endpointDataReferences.get(agreementId);
        }

        // Get data through data plane
        String data = "";
        try {
            data = this.httpProxyService.sendGETRequest(dataReference,subUrl,queryParams);
            Matcher dataMatcher=RESPONSE_PATTERN.matcher(data);
            while(dataMatcher.matches()) {
                data=dataMatcher.group("embeddedData");
                data=data.replace("\\\"","\"").replace("\\\\","\\");
                dataMatcher=RESPONSE_PATTERN.matcher(data);
            }
        } catch (IOException e) {
            monitor.severe("Call against consumer control plane failed!", e);
        }
        return data;
    }

    @POST
    @Path("/{assetId}/{subUrl:.+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postWrapper(@QueryParam("provider-connector-url") String providerConnectorUrl, @PathParam("assetId") String assetId, @PathParam("subUrl") String subUrl, String body, @Context UriInfo uriInfo) throws InterruptedException {

        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        String providerControlPlaneIDSUrl = String.format(providerControlPlaneFormat,providerConnectorUrl);

        // Initialize and negotiate everything
        // TODO do this only if no agreement is already existing
        var agreementId = initializeContractNegotiation(providerControlPlaneIDSUrl, assetId);

        // Initiate transfer process
        transferProcessService.initiateHttpProxyTransferProcess(
                agreementId,
                assetId,
                consumerControlPlaneBaseUrl,
                providerControlPlaneIDSUrl,
                header
        );

        EndpointDataReference dataReference = null;
        while (dataReference == null) {
            Thread.sleep(1000);
            dataReference = endpointDataReferences.get(agreementId);
        }

        // Get data through data plane
        String data = "";
        try {
            data = this.httpProxyService.sendPOSTRequest(
                    dataReference,
                    subUrl,
                    queryParams,
                    body,
                    Objects.requireNonNull(okhttp3.MediaType.parse("application/json"))
            );
            Matcher dataMatcher=RESPONSE_PATTERN.matcher(data);
            while(dataMatcher.matches()) {
                data=dataMatcher.group("embeddedData");
                data=data.replace("\\\"","\"").replace("\\\\","\\");
                dataMatcher=RESPONSE_PATTERN.matcher(data);
            }
        } catch (IOException e) {
            monitor.severe("Call against consumer control plane failed!", e);
        }
        return data;
    }

    @POST
    @Path("/proxy-callback")
    public void pullData(EndpointDataReference dataReference) {
        endpointDataReferences.put(dataReference.getContractId(), dataReference);
        monitor.debug("Endpoint Data Reference received and stored for agreement: " + dataReference.getContractId());
    }

    private String initializeContractNegotiation(String providerConnectorIDSUrl, String assetId) throws InterruptedException {

        var contractOffer = contractOfferService.findContractOffer4AssetId(
                assetId,
                consumerControlPlaneBaseUrl,
                providerConnectorIDSUrl,
                header
        );

        // Initiate negotiation
        var contractOfferRequest = ContractOfferRequest.Builder.newInstance()
                .contractOffer(contractOffer)
                .connectorId("provider")
                .connectorAddress(providerConnectorIDSUrl)
                .protocol("ids-multipart")
                .build();
        var negotiationId = contractNegotiationService.initiateNegotiation(
                contractOfferRequest,
                consumerControlPlaneBaseUrl,
                header
        );

        // Check negotiation state
        NegotiationStatusResponse negotiationResponse = null;

        while (negotiationResponse == null || !Objects.equals(negotiationResponse.getStatus(), "CONFIRMED")) {
            Thread.sleep(1000);
            negotiationResponse = contractNegotiationService.getNegotiationState(
                    negotiationId,
                    consumerControlPlaneBaseUrl,
                    header
            );
        }

        return negotiationResponse.getContractAgreementId();
    }
}
