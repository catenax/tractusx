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

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.ProvisionedResource;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusChecker;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Status checked responsible for providing a file transfer status.
 */
public class FileStatusChecker implements StatusChecker {

    private final Monitor monitor;

    /**
     * FileStatusChecker constructor
     * @param monitor Logger
     */
    public FileStatusChecker(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public boolean isComplete(TransferProcess transferProcess, List<ProvisionedResource> list) {
        boolean exists = getDestinationFile(transferProcess).exists();
        if (exists) {
            monitor.info("FileStatusChecker detected completed transfer process");
        }
        return exists;
    }

    private File getDestinationFile(TransferProcess transferProcess) {
        var destination = transferProcess.getDataRequest().getDataDestination();
        var destinationPath = Path.of(destination.getProperty("path"));

        File file = destinationPath.toFile();
        if (file.exists() && file.isDirectory()) {
            var source = transferProcess.getDataRequest().getDataEntry().getCatalogEntry().getAddress();
            var sourceFileName = source.getProperty("filename");
            return Path.of(destinationPath.toString(), sourceFileName).toFile();
        }

        return file;
    }
}
