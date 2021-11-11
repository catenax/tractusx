package org.eclipse.dataspaceconnector.extensions.transferprocess;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.util.Collections.emptyList;

public class TransferProcessFileHandler implements StatusChecker {

    private final Monitor monitor;

    public TransferProcessFileHandler(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public boolean isComplete(TransferProcess transferProcess, List<ProvisionedResource> list) {
        boolean exists = getDestinationPath(transferProcess).toFile().exists();
        if (exists) {
            monitor.info("FileStatusChecker detected completed transfer process");
        }
        return exists;
    }

    public TransferProcessFile parse(TransferProcess process) {
        Path destinationPath = getDestinationPath(process);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(destinationPath.toFile(), TransferProcessFile.class);
        } catch (IOException e) {
            monitor.severe("Could not retrieve results for process " + process.getId(), e);
        }

        return new TransferProcessFile("error", emptyList());
    }

    private Path getDestinationPath(TransferProcess process) {
        var destination = process.getDataRequest().getDataDestination();
        var destinationPath = Path.of(destination.getProperty("path"));

        File file = destinationPath.toFile();
        if (file.exists() && file.isDirectory()) {
            destinationPath = Path.of(destinationPath.toString(), process.getDataRequest().getDataEntry().getId());
        }

        return destinationPath;
    }
}
