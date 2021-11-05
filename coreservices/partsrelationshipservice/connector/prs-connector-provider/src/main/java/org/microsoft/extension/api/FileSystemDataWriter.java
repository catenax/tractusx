package org.microsoft.extension.api;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class FileSystemDataWriter implements DataWriter {
    private final Monitor monitor;

    public FileSystemDataWriter(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void write(DataAddress destination, String data) {

        var destinationPath = Path.of(destination.getProperty("destinationPath"));

        if (!destinationPath.toFile().exists()) { //interpret as directory
            monitor.info("Destination path " + destinationPath + " does not exist, will attempt to create");
            try {
                Files.createDirectory(destinationPath);
            } catch (IOException e) {
                String message = "Error creating directory: " + e.getMessage();
                monitor.severe(message);
                throw new RuntimeException(message, e);
            }
        } else if (destinationPath.toFile().isDirectory()) {
            destinationPath = Path.of(destinationPath.toString(), Instant.now().toString() + "_by_vin.json");
        }

        try {
            Files.write(destinationPath, data.getBytes());
        } catch (IOException e) {
            String message = "Error copying file " + e.getMessage();
            monitor.severe(message);
            throw new RuntimeException(message, e);
        }

    }
}
