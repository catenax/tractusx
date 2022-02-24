/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework;

/**
 * Any exception carrying a status
 */
public class StatusException extends Exception {
    public int getStatus() {
        return status;
    }

    private int status = 500;

    public StatusException(String message, Throwable inner) {
        super(message, inner);
    }

    public StatusException(String message) {
        super(message);
    }

    public StatusException(String message, Throwable inner, int status) {
        super(message, inner);
        this.status = status;
    }

    public StatusException(String message, int status) {
        super(message);
        this.status = status;
    }

}