//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package org.eclipse.dataspaceconnector.extensions.api;


import lombok.Data;

/**
 * JSON payload for file transfer request.
 */
@Data
public class FileRequest {

    /**
     * Path of file source.
     */
    private String filename;

    /**
     * Provider connector address to send the message to.
     */
    private String connectorAddress;

    /**
     * Destination path where the file should be copied.
     */
    private String destinationPath;
}
