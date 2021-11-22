//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.service;

import lombok.Builder;
import lombok.Value;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;

/**
 * XXX.
 */
@Value
@Builder
public class StatusResponse {
    /**
     * XXX.
     */
    private final TransferProcessStates status;
    /**
     * XXX.
     */
    private final String sasToken;
}
