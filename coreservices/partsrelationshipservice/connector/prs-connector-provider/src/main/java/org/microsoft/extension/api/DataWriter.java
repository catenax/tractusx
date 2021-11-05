package org.microsoft.extension.api;

import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

public interface DataWriter {
    void write(DataAddress destination, String data);
}
