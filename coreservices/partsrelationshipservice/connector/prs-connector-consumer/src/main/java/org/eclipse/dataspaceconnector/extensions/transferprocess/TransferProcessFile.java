package org.eclipse.dataspaceconnector.extensions.transferprocess;

import java.util.Collection;

public class TransferProcessFile {

    private String value;
    private Collection<SequentTransferProcess> transferProcesses;

    public TransferProcessFile() {
    }

    public TransferProcessFile(String value, Collection<SequentTransferProcess> transferProcesses) {
        this.value = value;
        this.transferProcesses = transferProcesses;
    }

    public String getValue() {
        return value;
    }

    public Collection<SequentTransferProcess> getTransferProcesses() {
        return transferProcesses;
    }
}
