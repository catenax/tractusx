package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileTransferFlowController implements DataFlowController {
    private final Monitor monitor;
    private final TypeManager typeManager;

    public FileTransferFlowController(Monitor monitor, TypeManager typeManager) {
        this.monitor = monitor;
        this.typeManager = typeManager;
    }

    @Override
    public boolean canHandle(DataRequest dataRequest) {
        return dataRequest.getDataDestination().getType().equalsIgnoreCase("file");
    }

    @Override
    public @NotNull DataFlowInitiateResponse initiateFlow(DataRequest dataRequest) {
        var source = dataRequest.getDataEntry().getCatalogEntry().getAddress();
        var destination = dataRequest.getDataDestination();

        // verify source path
        String sourceFileName = source.getProperty("filename");
        var sourcePath = Path.of(source.getProperty("path"), sourceFileName);
        if (!sourcePath.toFile().exists()) {
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, "source file " + sourcePath + " does not exist!");
        }

        // verify destination path
        var destinationPath = Path.of(destination.getProperty("path"));
        var destinationParentDirPath = destinationPath.getParent();

        if (!destinationParentDirPath.toFile().exists()) {
            monitor.info("Destination directory " + destinationParentDirPath + " does not exist, will attempt to create");
            try {
                Files.createDirectory(destinationParentDirPath);
            } catch (IOException e) {
                String message = "Error creating directory: " + e.getMessage();
                monitor.severe(message);
                return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
            }
        } else if (destinationPath.toFile().isDirectory()) {
            destinationPath = Path.of(destinationPath.toString(), dataRequest.getDataEntry().getId());
        }

        try {
            Thread.sleep(2000); // introduce delay to simulate data transfer work
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException | InterruptedException e) {
            String message = "Error copying file " + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);

        }

        return DataFlowInitiateResponse.OK;
    }

}