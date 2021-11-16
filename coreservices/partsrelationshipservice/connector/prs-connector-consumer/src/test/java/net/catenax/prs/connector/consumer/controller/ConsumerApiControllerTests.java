package net.catenax.prs.connector.consumer.controller;

import com.github.javafaker.Faker;
import jakarta.ws.rs.core.Response;
import net.catenax.prs.connector.consumer.middleware.RequestMiddleware;
import net.catenax.prs.connector.consumer.service.ConsumerService;
import net.catenax.prs.connector.requests.FileRequest;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerApiControllerTests {

    @Spy
    Monitor monitor = new ConsoleMonitor();

    @Spy
    RequestMiddleware handler = new RequestMiddleware(monitor);

    @Mock
    ConsumerService service;

    @InjectMocks
    ConsumerApiController controller;

    String processId = UUID.randomUUID().toString();

    Faker faker = new Faker();

    TransferProcessStates status = faker.options().option(TransferProcessStates.class);

    FileRequest fileRequest = FileRequest.builder()
            .connectorAddress(faker.internet().url())
            .destinationPath(faker.file().fileName())
            .build();

    TransferInitiateResponse transferResponse = TransferInitiateResponse.Builder.newInstance()
            .id(faker.lorem().characters())
            .status(faker.options().option(ResponseStatus.class)).build();

    @Test
    public void checkHealth_Returns() {
        assertThat(controller.checkHealth()).isEqualTo("I'm alive!");
    }


    @Test
    public void initiateTransfer_WhenFailure_ReturnsError() {
        // Act
        var response = controller.initiateTransfer(fileRequest);
        // Assert
        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getEntity()).isNull();
    }

    @Test
    public void initiateTransfer_WhenSuccess_ReturnsTransferId() {
        // Arrange
        when(service.initiateTransfer(fileRequest)).thenReturn(Optional.of(transferResponse));
        // Act
        var response = controller.initiateTransfer(fileRequest);
        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(transferResponse.getId());
    }

    @Test
    public void getStatus_WhenNotFound_ReturnsNotFound() {
        // Act
        var response = controller.getStatus(processId);
        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getStatus_WhenSuccess_ReturnsStatus() {
        // Arrange
        when(service.getStatus(processId)).thenReturn(Optional.of(status));
        // Act
        var response = controller.getStatus(processId);
        // Assert
        assertThat(response.getEntity()).isEqualTo(status.name());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
}
