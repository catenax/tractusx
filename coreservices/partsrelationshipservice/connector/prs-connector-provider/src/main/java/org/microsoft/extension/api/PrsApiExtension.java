package org.microsoft.extension.api;

import org.eclipse.dataspaceconnector.policy.model.*;
import org.eclipse.dataspaceconnector.spi.metadata.MetadataStore;
import org.eclipse.dataspaceconnector.spi.policy.PolicyRegistry;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowManager;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;

import java.util.Set;

import static org.eclipse.dataspaceconnector.policy.model.Operator.IN;

public class PrsApiExtension implements ServiceExtension {

    public static final String USE_EU_POLICY = "use-eu";
    private static final String PRS_URL_CONFIG = "prs.url";


    // TODO: is it needed?
    @Override
    public Set<String> requires() {
        return Set.of("edc:webservice", PolicyRegistry.FEATURE);
    }

    @Override
    public void initialize(ServiceExtensionContext context) {

        var dataFlowMgr = context.getService(DataFlowManager.class);
        String setting = context.getSetting(PRS_URL_CONFIG, "http://localhost:8080");
        context.getMonitor().info(setting);
        var flowController = new PrsApiFlowController(context.getMonitor(), new PrsApiCaller(setting),
                new FileSystemDataWriter(context.getMonitor()));
        dataFlowMgr.register(flowController);
        context.getMonitor().info("PRS API extension initialized!");

        savePolicies(context);
        registerDataEntries(context);
    }

    private void savePolicies(ServiceExtensionContext context) {
        PolicyRegistry policyRegistry = context.getService(PolicyRegistry.class);

        LiteralExpression spatialExpression = new LiteralExpression("ids:absoluteSpatialPosition");
        var euConstraint = AtomicConstraint.Builder.newInstance().leftExpression(spatialExpression).operator(IN).rightExpression(new LiteralExpression("eu"))
                .build();
        var euUsePermission = Permission.Builder.newInstance().action(Action.Builder.newInstance().type("idsc:USE").build()).constraint(euConstraint).build();
        var euPolicy = Policy.Builder.newInstance().id(USE_EU_POLICY).permission(euUsePermission).build();
        policyRegistry.registerPolicy(euPolicy);
    }

    private void registerDataEntries(ServiceExtensionContext context) {
        var metadataStore = context.getService(MetadataStore.class);

        DataEntry entry1 = DataEntry.Builder.newInstance().id("prs-api").policyId(USE_EU_POLICY).build();
        metadataStore.save(entry1);
    }

}
