//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
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

/**
 * Extension to transfer file.
 */
@SuppressWarnings("PMD.CommentRequired")
public class FileTransferExtension implements ServiceExtension {

    public static final String USE_EU_POLICY = "use-eu";

    @Override
    public Set<String> requires() {
        return Set.of("edc:webservice", PolicyRegistry.FEATURE);
    }

    @Override
    public void initialize(final ServiceExtensionContext context) {

        final var dataFlowMgr = context.getService(DataFlowManager.class);
        final var flowController = new FileTransferFlowController(context.getMonitor());
        dataFlowMgr.register(flowController);


        registerDataEntries(context);
        savePolicies(context);
        context.getMonitor().info("File Transfer Extension initialized!");
    }

    private void savePolicies(final ServiceExtensionContext context) {
        final PolicyRegistry policyRegistry = context.getService(PolicyRegistry.class);

        final LiteralExpression spatialExpression = new LiteralExpression("ids:absoluteSpatialPosition");
        final var euConstraint = AtomicConstraint.Builder.newInstance().leftExpression(spatialExpression).operator(IN).rightExpression(new LiteralExpression("eu")).build();
        final var euUsePermission = Permission.Builder.newInstance().action(Action.Builder.newInstance().type("idsc:USE").build()).constraint(euConstraint).build();
        final var euPolicy = Policy.Builder.newInstance().id(USE_EU_POLICY).permission(euUsePermission).build();
        policyRegistry.registerPolicy(euPolicy);
    }

    private void registerDataEntries(final ServiceExtensionContext context) {
        final var metadataStore = context.getService(MetadataStore.class);

        final GenericDataCatalogEntry file1 = GenericDataCatalogEntry.Builder.newInstance()
                .property("type", "File")
                .property("path", "/tmp/copy/source")
                .property("filename", "test-document.txt")
                .build();

        final DataEntry entry1 = DataEntry.Builder.newInstance().id("test-document").policyId(USE_EU_POLICY).catalogEntry(file1).build();
        metadataStore.save(entry1);
    }
}
