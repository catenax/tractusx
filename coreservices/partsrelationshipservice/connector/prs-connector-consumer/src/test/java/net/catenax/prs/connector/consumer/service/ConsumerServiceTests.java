package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Map;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTests {

    public static final String STORAGE_ACCOUNT_NAME = "StorageAccountName";

    @Spy
    Monitor monitor = new ConsoleMonitor();

    @Mock
    TransferProcessStore processStore;

    @Mock
    TransferProcessManager transferProcessManager;

    ConsumerService service;

    String processId = UUID.randomUUID().toString();

    Faker faker = new Faker();

    @Captor
    ArgumentCaptor<DataRequest> dataRequestCaptor;


    @BeforeEach
    public void before() {
        service = new ConsumerService(monitor, transferProcessManager, processStore, STORAGE_ACCOUNT_NAME);
    }

    @Test
    public void getStatus_WhenProcessNotInStore_ReturnsEmpty() {
        // Act
        var response = service.getStatus(processId);
        // Assert
        assertThat(response).isEmpty();
    }

    @Test
    public void getStatus_WhenProcessInStore_ReturnsState() {
        // Arrange
        TransferProcess transferProcess = mock(TransferProcess.class);
        when(transferProcess.getState()).thenReturn(TransferProcessStates.PROVISIONING.code());
        when(processStore.find(processId)).thenReturn(transferProcess);
        // Act
        var response = service.getStatus(processId);
        // Assert
        assertThat(response).contains(TransferProcessStates.PROVISIONING);
    }

    @Test
    public void initiateTransfer_WhenFileRequestValid_ReturnsProcessId() throws JsonProcessingException {
        // Arrange
        PartsTreeByObjectIdRequest partsTreeRequest = PartsTreeByObjectIdRequest.builder()
                .oneIDManufacturer(faker.company().name())
                .objectIDManufacturer(faker.lorem().characters(10, 20))
                .view("AS_BUILT")
                .depth(faker.number().numberBetween(1, 5))
                .build();

        FileRequest fileRequest = FileRequest.builder()
                .connectorAddress(faker.internet().url())
                .destinationPath(faker.file().fileName())
                .partsTreeRequest(partsTreeRequest)
                .build();

        String serializedPartsTreeRequest = String.format("{\"oneIDManufacturer\":\"%s\",\"objectIDManufacturer\":\"%s\",\"view\":\"%s\",\"aspect\":%s,\"depth\":%s}",
            partsTreeRequest.getOneIDManufacturer(), partsTreeRequest.getObjectIDManufacturer(), partsTreeRequest.getView(), partsTreeRequest.getAspect(), partsTreeRequest.getDepth());

        when(transferProcessManager.initiateConsumerRequest(any(DataRequest.class)))
                .thenReturn(okResponse());
        ObjectMapper mapper = new ObjectMapper();

        // Act
        var response = service.initiateTransfer(fileRequest);
        // Assert
        assertThat(response).isPresent();
        // Verify that initiateConsumerRequest got called with correct DataRequest input.
        verify(transferProcessManager).initiateConsumerRequest(dataRequestCaptor.capture());
        var expectedDataRequest = DataRequest.Builder.newInstance()
                .id(dataRequestCaptor.getValue().getId()) // Get the id generated by the provider.
                .connectorAddress(fileRequest.getConnectorAddress())
                .protocol("ids-rest")
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance()
                        .id("prs-request")
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE)
                        .property("account", STORAGE_ACCOUNT_NAME)
                        .build())
                .properties(Map.of(
                    "prs-request-parameters", serializedPartsTreeRequest,
                    "prs-destination-path", fileRequest.getDestinationPath()
                ))
                .managedResources(true)
                .build();

        assertThatJson(expectedDataRequest).isEqualTo(dataRequestCaptor.getValue());
    }

    private TransferInitiateResponse okResponse() {
        return TransferInitiateResponse.Builder.newInstance()
                .id(UUID.randomUUID().toString())
                .status(ResponseStatus.OK)
                .build();
    }
}
