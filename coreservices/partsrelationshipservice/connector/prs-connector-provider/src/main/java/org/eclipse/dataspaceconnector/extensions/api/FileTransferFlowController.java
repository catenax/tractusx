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
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Handles a data flow to transfer a file.
 */
@SuppressWarnings({"PMD.CommentRequired", "PMD.GuardLogStatement"})
public class FileTransferFlowController implements DataFlowController {

    // Removed BeanMembersShouldSerialize rule because Monitor is final,
    // so adding transient will not have any impact.
    @SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
    private final Monitor monitor;
    // Delay used to simulate data transfer work.
    private static final int DELAY = 2000;

    /**
     * @param monitor Logger
     */
    public FileTransferFlowController(final Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public boolean canHandle(final DataRequest dataRequest) {
        return "file".equalsIgnoreCase(dataRequest.getDataDestination().getType());
    }

    @Override
    public @NotNull DataFlowInitiateResponse initiateFlow(final DataRequest dataRequest) {
        // verify source path
        final var source = dataRequest.getDataEntry().getCatalogEntry().getAddress();
        final String sourceFileName = source.getProperty("filename");
        final var sourcePath = Path.of(source.getProperty("path"), sourceFileName);
        if (!sourcePath.toFile().exists()) {
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, "source file " + sourcePath + " does not exist!");
        }

        // verify destination path
        final var destination = dataRequest.getDataDestination();
        var destinationPath = Path.of(destination.getProperty("path"));
        final var destinationParentDirPath = destinationPath.getParent();
        final var destinationDirectoryDoesNotExists = !destinationParentDirPath.toFile().exists();
        if (destinationDirectoryDoesNotExists) {
            monitor.info("Destination directory " + destinationParentDirPath + " does not exist, will attempt to create");
            try {
                Files.createDirectory(destinationParentDirPath);
            } catch (IOException e) {
                final String message = "Error creating directory: " + e.getMessage();
                monitor.severe(message);
                return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
            }
        } else if (destinationPath.toFile().isDirectory()) {
            destinationPath = Path.of(destinationPath.toString(), sourceFileName);
        }

        try {
            Thread.sleep(DELAY); // introduce delay to simulate data transfer work
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException | InterruptedException e) {
            final String message = "Error copying file " + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);

        }

        return DataFlowInitiateResponse.OK;
    }

}
