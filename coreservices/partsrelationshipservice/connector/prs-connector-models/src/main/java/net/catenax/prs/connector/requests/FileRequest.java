//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * JSON payload for file transfer request.
 */
@Value
@Jacksonized
@Builder(toBuilder = true)
public class FileRequest {
    /**
     * Provider connector address to send the message to.
     */
    @NotBlank
    private String connectorAddress;

    /**
     * Destination path where the file should be copied.
     */
    @NotBlank
    private String destinationPath;

    /**
     * Parts Tree Request.
     */
    @Valid
    @NotNull
    private PartsTreeByObjectIdRequest partsTreeRequest;
}
