package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTests {

    public static final String STORAGE_ACCOUNT_NAME = "storageAccount";
    public static final String COMPLETE_SUFFIX = ".complete";

    @Spy
    Monitor monitor = new ConsoleMonitor();

    @Mock
    TransferProcessStore processStore;

    @Mock
    TransferProcessManager transferProcessManager;

    @Mock
    BlobStoreApi blobStoreApi;

    ConsumerService service;

    String processId = UUID.randomUUID().toString();

    Faker faker = new Faker();

    static final ObjectMapper MAPPER = new ObjectMapper();

    @Captor
    ArgumentCaptor<DataRequest> dataRequestCaptor;

    @Captor
    ArgumentCaptor<OffsetDateTime> offsetDateTimeCaptor;

    @BeforeEach
    public void before() {
        service = new ConsumerService(monitor, transferProcessManager, processStore, blobStoreApi, STORAGE_ACCOUNT_NAME);
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
        assertThat(response).isNotEmpty();
        assertThat(response.get()).usingRecursiveComparison()
            .isEqualTo(StatusResponse.builder()
                .status(TransferProcessStates.PROVISIONING)
                .build());
    }

    @Test
    public void getStatus_WhenProcessInStoreCompleted_ReturnsSASUrl() throws MalformedURLException {
        // Arrange
        String containerName = faker.lorem().word();
        String destinationPath = faker.lorem().word();
        String sasToken = faker.lorem().characters(10, 15);
        TransferProcess transferProcess = mock(TransferProcess.class);
        DataRequest dataRequest = DataRequest.Builder.newInstance()
                        .dataDestination(DataAddress.Builder.newInstance()
                                .type(AzureBlobStoreSchema.TYPE)
                                .property(AzureBlobStoreSchema.CONTAINER_NAME, containerName)
                                .build())
                        .properties(Map.of("prs-destination-path", destinationPath))
                        .build();

        when(transferProcess.getState()).thenReturn(TransferProcessStates.COMPLETED.code());
        when(transferProcess.getDataRequest()).thenReturn(dataRequest);
        when(processStore.find(processId)).thenReturn(transferProcess);
        when(blobStoreApi.createContainerSasToken(eq(STORAGE_ACCOUNT_NAME), eq(containerName), eq("r"), offsetDateTimeCaptor.capture())).thenReturn(sasToken);
        // Act
        var response = service.getStatus(processId);
        // Assert
        assertThat(response).isNotEmpty();
        assertThat(new URL(response.get().getSasToken()))
                .hasProtocol("https")
                .hasHost(STORAGE_ACCOUNT_NAME + ".blob.core.windows.net")
                .hasPath("/" + containerName + "/" + destinationPath)
                .hasParameter(sasToken);
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

        String serializedPartsTreeRequest = MAPPER.writeValueAsString(partsTreeRequest);

        when(transferProcessManager.initiateConsumerRequest(any(DataRequest.class)))
                .thenReturn(okResponse());

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
                    "prs-destination-path", fileRequest.getDestinationPath() + COMPLETE_SUFFIX
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
