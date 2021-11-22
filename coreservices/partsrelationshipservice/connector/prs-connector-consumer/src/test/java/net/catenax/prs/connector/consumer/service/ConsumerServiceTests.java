package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.connector.job.*;
import net.catenax.prs.connector.requests.FileRequest;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTests {

    static final ObjectMapper MAPPER = new ObjectMapper();
    @Mock
    JobStore jobStore;
    @Mock
    JobOrchestrator jobOrchestrator;
    @Spy
    Monitor monitor = new ConsoleMonitor();
    @InjectMocks
    ConsumerService service;
    Faker faker = new Faker();
    String jobId = UUID.randomUUID().toString();
    MultiTransferJob job = MultiTransferJob.builder()
            .state(faker.options().option(JobState.class))
            .build();
    @Captor
    ArgumentCaptor<Map<String, String>> jobDataCaptor;

    private final RequestMother generate = new RequestMother();
    private final FileRequest fileRequest = generate.fileRequest();

    @Test
    public void getStatus_WhenProcessNotInStore_ReturnsEmpty() {
        // Act
        var response = service.getStatus(jobId);
        // Assert
        assertThat(response).isEmpty();
    }

    @Test
    public void getStatus_WhenProcessInStore_ReturnsState() {
        // Arrange
        when(jobStore.find(jobId)).thenReturn(Optional.of(job));
        // Act
        var response = service.getStatus(jobId);
        // Assert
        assertThat(response).isNotEmpty();
        assertThat(response.get()).usingRecursiveComparison()
            .isEqualTo(StatusResponse.builder()
                .status(job.getState())
                .build());
    }

    @Test
    public void initiateTransfer_WhenFileRequestValid_ReturnsProcessId() throws JsonProcessingException {
        // Arrange
        String serializedRequest = MAPPER.writeValueAsString(fileRequest);

        when(jobOrchestrator.startJob(any(Map.class)))
                .thenReturn(okResponse());

        // Act
        var response = service.initiateTransfer(fileRequest);
        // Assert
        assertThat(response).isPresent();
        // Verify that startJob got called with correct job parameters.
        verify(jobOrchestrator).startJob(jobDataCaptor.capture());
        assertThat(jobDataCaptor.getValue())
                .isEqualTo(Map.of(ConsumerService.PARTS_REQUEST_KEY, serializedRequest));
    }

    private JobInitiateResponse okResponse() {
        return JobInitiateResponse.builder()
                .jobId(UUID.randomUUID().toString())
                .status(ResponseStatus.OK)
                .build();
    }
}
