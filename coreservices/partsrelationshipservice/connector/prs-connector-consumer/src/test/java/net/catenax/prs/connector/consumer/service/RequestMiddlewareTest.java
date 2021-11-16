package net.catenax.prs.connector.consumer.service;

import com.github.javafaker.Faker;
import jakarta.ws.rs.core.Response;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMiddlewareTest {

    Monitor monitor = new ConsoleMonitor();
    RequestMiddleware sut = new RequestMiddleware(monitor);
    Faker faker = new Faker();
    Response.Status status = faker.options().option(Response.Status.class);

    @Test
    void invoke_OnSuccess_ReturnsResponse() {
        // Act
        var result = sut.invoke(() -> Response.status(status).build());

        // Assert
        assertThat(result.getStatus()).isEqualTo(status.getStatusCode());
    }

    @Test
    void invoke_OnException_ReturnsErrorResponse() {
        // Act
        var result = sut.invoke(() -> {
            throw new ArrayStoreException();
        });

        // Assert
        assertThat(result.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }
}