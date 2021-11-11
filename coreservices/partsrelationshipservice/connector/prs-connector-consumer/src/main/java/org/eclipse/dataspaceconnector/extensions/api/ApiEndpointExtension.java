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

import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;

import java.util.Set;

public class ApiEndpointExtension implements ServiceExtension {

    @Override
    public Set<String> requires() {
        return Set.of("edc:webservice", "dataspaceconnector:transferprocessstore", "dataspaceconnector:blobstoreapi");
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var webService = context.getService(WebService.class);
        var processManager = context.getService(TransferProcessManager.class);
        var processStore = context.getService(TransferProcessStore.class);
        var blobApi = context.getService(BlobStoreApi.class);
        webService.registerController(new ConsumerApiController(context.getMonitor(), processManager, processStore, blobApi,
                context.getSetting("edc.storage.account.name", "prsspikestorage")));
    }
}
