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
 * Exception thrown in case of inconsistent data.
 */
public class CompositionException extends RuntimeException {
    /**
     * Generate a new instance of a {@link CompositionException}
     *
     * @param message Exception message.
     */
    public CompositionException(final String message) {
        super(message);
    }
}
