package net.catenax.prs.connector.consumer.middleware;

import com.github.javafaker.Faker;
import jakarta.ws.rs.core.Response;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestMiddlewareTest {

    @Mock
    Monitor monitor;

    @InjectMocks
    RequestMiddleware sut;

    Faker faker = new Faker();
    Response.Status status = faker.options().option(Response.Status.class);
    RuntimeException exception = new RuntimeException(faker.lorem().sentence());

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
            throw exception;
        });

        // Assert
        assertThat(result.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        verify(monitor).warning("Server error: " + exception.getMessage(), exception);
    }
}