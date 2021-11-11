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

    private String filename;

    private String connectorAddress;

    private String destinationPath;
}
