package org.eclipse.dataspaceconnector.extensions.api;


import lombok.Data;

@Data
public class FileRequest {

    private String filename;

    private String connectorAddress;

    private String destinationPath;
}
