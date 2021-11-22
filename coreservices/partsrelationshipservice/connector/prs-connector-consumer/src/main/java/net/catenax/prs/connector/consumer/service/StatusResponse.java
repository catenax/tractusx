package net.catenax.prs.connector.consumer.service;

import lombok.Builder;
import lombok.Getter;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;

@Getter
@Builder
public class StatusResponse {
    private final TransferProcessStates status;
    private final String sasToken;
}
