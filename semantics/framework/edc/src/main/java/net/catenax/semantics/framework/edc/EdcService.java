/*
Copyright (c) 2021-2022 T-Systems International GmbH
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.edc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import net.catenax.semantics.framework.IdsConnector;
import net.catenax.semantics.framework.IdsRequest;
import net.catenax.semantics.framework.IdsResponse;
import net.catenax.semantics.framework.config.*;
import org.eclipse.dataspaceconnector.spi.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.boot.monitor.MonitorProvider;
//import org.eclipse.dataspaceconnector.dataloading.AssetLoader;
//import org.eclipse.dataspaceconnector.ids.spi.descriptor.IdsDescriptorService;
import org.eclipse.dataspaceconnector.ids.spi.descriptor.IdsDescriptorService;
import org.eclipse.dataspaceconnector.policy.model.*;
import org.eclipse.dataspaceconnector.spi.contract.offer.store.ContractDefinitionStore;
import org.eclipse.dataspaceconnector.spi.types.domain.contract.offer.ContractDefinition;
import org.eclipse.dataspaceconnector.spi.asset.AssetSelectorExpression;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.types.domain.asset.Asset;
import org.eclipse.dataspaceconnector.spi.types.domain.DataAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Conditional(EdcConfigurationCondition.class)
public class EdcService<Cmd extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation> implements IdsConnector {
    final private Config<Cmd,O,Ct,Co, T> configurationData;

    protected static final Logger logger = LoggerFactory.getLogger(EdcService.class);
    private static EdcRuntime edcRuntime;
    private ServiceExtensionContext context;

    public static final String USE_EU_POLICY = "use-eu";

    public static void bootstrap() {
        MonitorProvider.setInstance(new ConsoleMonitor());
    }

    public static void tearDown() {
    }

    @PostConstruct
    public void initEDC() {
        System.getProperties().put("edc.connector.name",configurationData.getConnectorId());
        System.getProperties().put("edc.ids.title","PWC Adapter EDC Connector");
        System.getProperties().put("edc.ids.description","An adapter-builtin EDC Connector");
        System.getProperties().put("edc.ids.id",configurationData.getConnectorId());
        System.getProperties().put("edc.ids.endpoint",configurationData.getConnectorUrl());
        System.getProperties().put("edc.ids.maintainer",configurationData.getPublisher());
        System.getProperties().put("edc.ids.curator","http://www.t-systems.com");
        System.getProperties().put("edc.ids.catalog.id",configurationData.getCatalogs().keySet().iterator().next());

        edcRuntime = new EdcRuntime() {
            @Override
            protected void onError(Exception e) {
                logger.error("Error booting EDC runtime", e);
            }
        };
        edcRuntime.start();
        this.context = edcRuntime.getServiceExtensionContext();
        //savePolicies(context);

        //var dataAddressResolver = context.getService(DataAddressResolver.class);
        //var dataFlowMgr = context.getService(DataFlowManager.class);

        //var flowController = new FileTransferFlowController(context.getMonitor(), dataAddressResolver);

        //dataFlowMgr.register(flowController);
    }

    private void savePolicies(ServiceExtensionContext context) {
        LiteralExpression spatialExpression = new LiteralExpression("ids:absoluteSpatialPosition");
        var euConstraint = AtomicConstraint.Builder.newInstance().leftExpression(spatialExpression).operator(Operator.IN).rightExpression(new LiteralExpression("eu")).build();
        var euUsePermission = Permission.Builder.newInstance().action(Action.Builder.newInstance().type("idsc:USE").build()).constraint(euConstraint).build();
        var euPolicy = Policy.Builder.newInstance().id(USE_EU_POLICY).permission(euUsePermission).build();
        
        ContractDefinition contractDefinition = ContractDefinition.Builder.newInstance()
                .id("1")
                .accessPolicy(euPolicy)
                .contractPolicy(euPolicy)
                .selectorExpression(AssetSelectorExpression.Builder.newInstance().whenEquals(Asset.PROPERTY_ID, "*").build())
                .build();

        ContractDefinitionStore contractStore = context.getService(ContractDefinitionStore.class);
        contractStore.save(contractDefinition);
    }

    @PreDestroy
    public void shutdownEDC() {
        // TODO
    }

    public Offer getOrCreateOffer(String title) {
        //AssetLoader loader = context.getService(AssetLoader.class);
        String assetPathSetting = title;

        DataAddress dataAddress = DataAddress.Builder.newInstance()
            .property("type", "PWC")
            .property("name",assetPathSetting)
            .build();

        String assetId = "edc";
        Asset asset = Asset.Builder.newInstance().id(assetId).build();

        //loader.accept(asset, dataAddress);
        Offer offer=new Offer();
        return offer;
    }

    @Override
    public Object getSelfDescription() {
        var descriptorService = context.getService(IdsDescriptorService.class);
        return descriptorService.description();
    }

    @Override
    public IdsResponse perform(IdsRequest request) {
        return null;
    }

}
