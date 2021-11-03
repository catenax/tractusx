//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.client.exceptions;

/**
 * API call exception.
 */
public class ApiClientException extends RuntimeException {
    /**
     * Generate a new instance of a {@link ApiClientException}
     *
     * @param cause Exception cause.
     */
    public ApiClientException(final Throwable cause) {
        super(cause);
    }
}
