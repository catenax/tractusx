package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.policy.model.Action;
import org.eclipse.dataspaceconnector.policy.model.AtomicConstraint;
import org.eclipse.dataspaceconnector.policy.model.LiteralExpression;
import org.eclipse.dataspaceconnector.policy.model.Permission;
import org.eclipse.dataspaceconnector.policy.model.Policy;
import org.eclipse.dataspaceconnector.spi.metadata.MetadataStore;
import org.eclipse.dataspaceconnector.spi.policy.PolicyRegistry;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowManager;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.GenericDataCatalogEntry;

import java.util.Set;

import static org.eclipse.dataspaceconnector.policy.model.Operator.IN;

public class FileTransferExtension implements ServiceExtension {

    public static final String USE_EU_POLICY = "use-eu";

    @Override
    public Set<String> requires() {
        return Set.of("edc:webservice", PolicyRegistry.FEATURE);
    }

    @Override
    public void initialize(ServiceExtensionContext context) {

        var dataFlowMgr = context.getService(DataFlowManager.class);
        var flowController = new FileTransferFlowController(context.getMonitor(), context.getTypeManager());
        dataFlowMgr.register(flowController);


        registerDataEntries(context);
        savePolicies(context);
        context.getMonitor().info("File Transfer Extension initialized!");
    }

    private void savePolicies(ServiceExtensionContext context) {
        PolicyRegistry policyRegistry = context.getService(PolicyRegistry.class);

        LiteralExpression spatialExpression = new LiteralExpression("ids:absoluteSpatialPosition");
        var euConstraint = AtomicConstraint.Builder.newInstance().leftExpression(spatialExpression).operator(IN).rightExpression(new LiteralExpression("eu")).build();
        var euUsePermission = Permission.Builder.newInstance().action(Action.Builder.newInstance().type("idsc:USE").build()).constraint(euConstraint).build();
        var euPolicy = Policy.Builder.newInstance().id(USE_EU_POLICY).permission(euUsePermission).build();
        policyRegistry.registerPolicy(euPolicy);
    }

    private void registerDataEntries(ServiceExtensionContext context) {
        var metadataStore = context.getService(MetadataStore.class);
        registerFile(metadataStore, "test-document-1", "test-document-1.json");
        registerFile(metadataStore, "test-document-2", "test-document-2.json");
        registerFile(metadataStore, "test-document-3", "test-document-3.json");
        registerFile(metadataStore, "test-document-4", "test-document-4.json");
    }

    private void registerFile(MetadataStore metadataStore, String id, String filename) {
        GenericDataCatalogEntry catalogEntry = GenericDataCatalogEntry.Builder.newInstance()
                .property("type", "File")
                .property("path", "/tmp/copy/source")
                .property("filename", filename)
                .build();

        DataEntry dataEntry = DataEntry.Builder.newInstance().id(id).policyId(USE_EU_POLICY).catalogEntry(catalogEntry).build();
        metadataStore.save(dataEntry);
    }
}
