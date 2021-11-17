//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.monitor;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.MonitorExtension;

/**
 * Extension adding logging monitor.
 */
public class LoggerMonitorExtension implements MonitorExtension {

    @Override
    public Monitor getMonitor() {
        return new LoggerMonitor();
    }
}
