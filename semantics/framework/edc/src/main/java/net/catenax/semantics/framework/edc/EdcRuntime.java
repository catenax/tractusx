/*
Copyright (c) 2021-2022 T-Systems International GmbH
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.edc;

import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.boot.system.runtime.BaseRuntime;

public class EdcRuntime extends BaseRuntime {
    public void start() {
        super.boot();
    }
    private ServiceExtensionContext serviceExtensionContext;

    @Override
    protected void initializeContext(ServiceExtensionContext context) {
        super.initializeContext(context);
        this.serviceExtensionContext = context;
    }

    public ServiceExtensionContext getServiceExtensionContext() {
        return serviceExtensionContext;
    }
}
