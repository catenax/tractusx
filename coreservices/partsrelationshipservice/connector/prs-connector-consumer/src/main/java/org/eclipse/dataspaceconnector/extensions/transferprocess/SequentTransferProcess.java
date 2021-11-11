package org.eclipse.dataspaceconnector.extensions.transferprocess;

public class SequentTransferProcess {
    private String file;
    private String connectorUrl;

    public SequentTransferProcess() {
    }

    public SequentTransferProcess(String file, String connectorUrl) {
        this.file = file;
        this.connectorUrl = connectorUrl;
    }

    public String getFile() {
        return file;
    }

    public String getConnectorUrl() {
        return connectorUrl;
    }
}
