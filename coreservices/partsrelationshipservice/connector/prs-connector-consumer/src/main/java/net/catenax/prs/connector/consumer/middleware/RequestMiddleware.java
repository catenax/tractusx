//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.middleware;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.util.function.Supplier;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Middleware for logging service exceptions.
 * <p>
 * Since the EDC framework does not currently allow extending the Jersey web server
 * with middleware, this middleware is used below the controller layer.
 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement") // Monitor doesn't offer guard statements
public class RequestMiddleware {

    /**
     * Logger.
     */
    private final Monitor monitor;

    /**
     * Invoke a service operation, processing any uncaught exceptions.
     *
     * @param supplier service operation
     * @return response from {@literal supplier}, or error response
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public Response invoke(final Supplier<Response> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException e) {
            monitor.warning("Server error: " + e.getMessage(), e);
            return Response.status(INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
