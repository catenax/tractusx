package net.catenax.prs.connector.consumer.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Middleware for logging service exceptions.
 * <p>
 * Since the EDC framework does not currently allow extending the Jersey web server
 * with middleware, this middleware is used below the controller layer.
 */
@RequiredArgsConstructor
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
    public Response invoke(ResponseSupplier supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            monitor.warning("Server error: " + e.getMessage());
            return Response.status(INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Represents a supplier of JAX-WS {@link Response}.
     */
    @FunctionalInterface
    public interface ResponseSupplier {

        /**
         * Gets a result.
         *
         * @return a result
         * @throws Exception on uncaught server error.
         */
        Response get() throws Exception;
    }
}
