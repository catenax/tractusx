/*
Copyright (c) 2021-2022 T-Systems International GmbH
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.edc;

import org.eclipse.dataspaceconnector.spi.asset.DataAddressResolver;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResult;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.jetbrains.annotations.NotNull;

public class FileTransferFlowController implements DataFlowController {
    private final Monitor monitor;
    private final DataAddressResolver dataAddressResolver;

    public FileTransferFlowController(Monitor monitor, DataAddressResolver dataAddressResolver) {
        this.monitor = monitor;
        this.dataAddressResolver = dataAddressResolver;
    }

    @Override
    public boolean canHandle(DataRequest dataRequest) {
        return dataRequest.getDataDestination().getType().equalsIgnoreCase("file");
    }

    @Override
    public @NotNull DataFlowInitiateResult initiateFlow(DataRequest dataRequest) {
        var source = dataAddressResolver.resolveForAsset(dataRequest.getAssetId());
        var destination = dataRequest.getDataDestination();
        monitor.info("got dataRequest : "+source+ " dest: "+destination);
        return DataFlowInitiateResult.success("");
    }

}