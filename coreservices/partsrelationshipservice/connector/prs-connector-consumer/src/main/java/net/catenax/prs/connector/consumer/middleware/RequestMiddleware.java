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

import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static java.lang.String.format;

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
     * Validator.
     */
    private final Validator validator;

    /**
     * XXX
     * @return XXX
     */
    public OngoingChain chain() {
        return new OngoingChain();
    }

    /**
     * XXX
     */
    public class OngoingChain {
        /**
         * XXX
         */
        private final Set<String> violations = new LinkedHashSet<>();

        /**
         * XXX
         * @param payload XXX
         * @param <T> XXX
         * @return XXX
         */
        public <T> OngoingChain validate(final T payload) {
            for (final var violation : validator.validate(payload)) {
                violations.add(format("%s %s", violation.getPropertyPath(), violation.getMessage()));
            }
            return this;
        }

        /**
         * Invoke a service operation, processing any uncaught exceptions.
         *
         * @param supplier service operation
         * @return response from {@literal supplier}, or error response
         */
        public Response invoke(final Supplier<Response> supplier) {
            if (!violations.isEmpty()) {
                final var message = violations.stream().map(s -> s + "\n").collect(Collectors.joining());
                monitor.warning("Validation failed: " + message);
                return Response.status(BAD_REQUEST)
                        .entity(message)
                        .build();
            }

            return doInvoke(supplier);
        }

        @SuppressWarnings("PMD.AvoidCatchingGenericException")
        private Response doInvoke(final Supplier<Response> supplier) {
            try {
                return supplier.get();
            } catch (RuntimeException e) {
                monitor.warning("Server error: " + e.getMessage(), e);
                return Response.status(INTERNAL_SERVER_ERROR)
                        .build();
            }
        }
    }
}
