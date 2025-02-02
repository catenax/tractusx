package org.eclipse.dataspaceconnector.plane.control;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.dataspaceconnector.dataloading.AssetLoader;
import org.eclipse.dataspaceconnector.spi.contract.offer.store.ContractDefinitionStore;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.asset.Asset;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.offer.ContractDefinition;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.eclipse.dataspaceconnector.spi.asset.AssetIndex;

import java.util.Map;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Path("/")
public class ControlPlaneController {
    private final Monitor monitor;
    private final AssetLoader assetLoader;
    private final ContractDefinitionStore contractDefinitionStore;
    private final TransferProcessStore transferProcessStore;
    private final AssetIndex assetIndex;

    public ControlPlaneController(Monitor monitor, AssetLoader assetLoader, ContractDefinitionStore contractDefinitionStore, TransferProcessStore transferProcessStore, AssetIndex assetIndex) {
        this.monitor = monitor;
        this.assetLoader = assetLoader;
        this.contractDefinitionStore = contractDefinitionStore;
        this.transferProcessStore = transferProcessStore;
        this.assetIndex=assetIndex;
    }

    // TODO: most of these api will be replaced by data management api

    @Path("/assets")
    @POST
    public String createAsset(Map<String, Map<String, String>> properties) {
        var assetProperties = properties.get("asset");
        var asset = Asset.Builder.newInstance().properties(assetProperties).build();

        var dataAddressProperties = properties.get("dataAddress");
        var dataAddress = DataAddress.Builder.newInstance().properties(dataAddressProperties).build();
        monitor.debug("Create asset: " + asset.getId());
        assetLoader.accept(asset, dataAddress);
        return asset.getId();
    }

    @Path("/assets/{name}")
    @GET
    public String findAsset(@PathParam("name") String name) {
        Asset asset=assetIndex.findById(name);
        if(asset!=null) {
            return asset.getId();
        } else {
            return "";
        }
    }

    @Path("/contractdefinitions")
    @POST
    public void createContractDefinition(ContractDefinition definition) {
        monitor.debug("Create contract definition: " + definition.getId());
        contractDefinitionStore.save(definition);
    }

    @Path("/transfers/{id}")
    @GET
    public TransferProcess getTransferProcess(@PathParam("id") String id) {
        return transferProcessStore.find(id);
    }
}
