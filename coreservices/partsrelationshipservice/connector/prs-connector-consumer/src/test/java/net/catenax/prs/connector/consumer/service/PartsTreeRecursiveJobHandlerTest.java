package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.consumer.registry.StubRegistryClient;
import net.catenax.prs.connector.job.JobState;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_REQUEST_PARAMETERS;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.PRS_REQUEST_ASSET_ID;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.PRS_REQUEST_POLICY_ID;
import static net.catenax.prs.connector.consumer.service.ConsumerService.CONTAINER_NAME_KEY;
import static net.catenax.prs.connector.consumer.service.ConsumerService.DESTINATION_PATH_KEY;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PartsTreeRecursiveJobHandlerTest {

    static final ObjectMapper MAPPER = new ObjectMapper();
    private final RequestMother generate = new RequestMother();
    private final FileRequest fileRequest = generate.fileRequest();
    Faker faker = new Faker();
    Monitor monitor = new ConsoleMonitor();
    String storageAccountName = faker.lorem().characters();
    String containerName = faker.lorem().word();
    String blobName = faker.lorem().word();
    String url = faker.internet().url();
    String url2 = faker.internet().url();
    ConsumerConfiguration configuration = ConsumerConfiguration.builder()
            .storageAccountName(storageAccountName)
            .build();
    PartsTreeRecursiveJobHandler sut;
    MultiTransferJob job = MultiTransferJob.builder()
            .jobId(faker.lorem().characters())
            .state(faker.options().option(JobState.class))
            .jobDatum(CONTAINER_NAME_KEY, containerName)
            .jobDatum(DESTINATION_PATH_KEY, blobName)
            .build();
    DataRequest.Builder requestBuilder;
    TransferProcess.Builder transferBuilder = TransferProcess.Builder.newInstance()
            .id(faker.lorem().characters());
    TransferProcess transfer = TransferProcess.Builder.newInstance()
            .id(faker.lorem().characters())
            .dataRequest(DataRequest.Builder.newInstance()
                    .dataDestination(DataAddress.Builder.newInstance()
                            .type(AzureBlobStoreSchema.TYPE)
                            .property(AzureBlobStoreSchema.ACCOUNT_NAME, storageAccountName)
                            .property(AzureBlobStoreSchema.CONTAINER_NAME, containerName)
                            .build())
                    .properties(Map.of(
                            DATA_REQUEST_PRS_DESTINATION_PATH, blobName
                    ))
                    .build())
            .build();
    @Captor
    ArgumentCaptor<byte[]> byteArrayCaptor;
    @Mock
    private StubRegistryClient registryClient;
    @Mock
    private BlobStoreApi blobStoreApi;

    @BeforeEach
    public void setUp() throws Exception {
        sut = new PartsTreeRecursiveJobHandler(monitor, configuration, blobStoreApi, new JsonUtil(monitor), registryClient);

        var serializedFileRequest = MAPPER.writeValueAsString(fileRequest);
        job = job.toBuilder().jobData(Map.of(ConsumerService.PARTS_REQUEST_KEY, serializedFileRequest)).build();
        String serializedPrsRequest = MAPPER.writeValueAsString(fileRequest.getPartsTreeRequest());
        requestBuilder = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString())
                .connectorAddress(url)
                .protocol("ids-rest")
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance()
                        .id(PRS_REQUEST_ASSET_ID)
                        .policyId(PRS_REQUEST_POLICY_ID)
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE)
                        .property(AzureBlobStoreSchema.ACCOUNT_NAME, configuration.getStorageAccountName())
                        .build())
                .properties(Map.of(
                        DATA_REQUEST_PRS_REQUEST_PARAMETERS, serializedPrsRequest,
                        DATA_REQUEST_PRS_DESTINATION_PATH, "partialPartsTree.complete"
                ))
                .managedResources(true);
    }

    @Test
    void initiate_WhenRegistryMatches_ReturnsOneDataRequest() {
        // Arrange
        when(registryClient.getUrl(fileRequest.getPartsTreeRequest()))
                .thenReturn(Optional.of(url));

        // Act
        var result = sut.initiate(job);

        // Assert
        var resultAsList = result.collect(Collectors.toList());
        assertThat(resultAsList).hasSize(1);

        // Verify that initiateConsumerRequest got called with correct DataRequest input.

        var expectedDataRequest = requestBuilder
                .id(resultAsList.get(0).getId())
                .build();
        assertThat(resultAsList.get(0))
                .usingRecursiveComparison()
                .isEqualTo(expectedDataRequest);
    }

    @Test
    void initiate_WhenRegistryNoMatch_ReturnsEmpty() {
        // Act
        var result = sut.initiate(job);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse_WhenRegistryNoMatch_ReturnsEmpty() {
        // Arrange
        transferBuilder.dataRequest(requestBuilder.build());

        // Act
        var result = sut.recurse(job, transferBuilder.build());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse_WhenRegistryMatchesAndUrlUnchanged_ReturnsEmpty() {
        // Arrange
        transferBuilder.dataRequest(requestBuilder.build());
        setUpRegistryToReturn(url);

        // Act
        var result = sut.recurse(job, transferBuilder.build());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse_WhenRegistryMatchesAndUrlChanged_ReturnsOneDataRequest() throws Exception {
        // Arrange
        transferBuilder.dataRequest(requestBuilder.build());
        setUpRegistryToReturn(url2);

        // Act
        var result = sut.recurse(job, transferBuilder.build());

        // Assert
        var resultAsList = result.collect(Collectors.toList());
        assertThat(resultAsList).hasSize(1);

        // Verify that initiateConsumerRequest got called with correct DataRequest input.

        var serializedPrsRequest = MAPPER.writeValueAsString(
                fileRequest.getPartsTreeRequest()
                        .toBuilder()
                        .oneIDManufacturer("CAXLBRHHQAJAIOZZ")
                        .build()
        );
        var expectedDataRequest = requestBuilder
                .id(resultAsList.get(0).getId())
                .connectorAddress(url2)
                .processId(null)
                .properties(Map.of(
                        DATA_REQUEST_PRS_REQUEST_PARAMETERS, serializedPrsRequest,
                        DATA_REQUEST_PRS_DESTINATION_PATH, "partialPartsTree.complete"
                ))
                .build();
        assertThat(resultAsList.get(0))
                .usingRecursiveComparison()
                .isEqualTo(expectedDataRequest);
    }

    private PartsTreeByObjectIdRequest setUpRegistryToReturn(String returnedUrl) {
        PartsTreeByObjectIdRequest caxlbrhhqajaiozz = fileRequest.getPartsTreeRequest().toBuilder()
                .oneIDManufacturer("CAXLBRHHQAJAIOZZ")
                .build();
        when(registryClient.getUrl(caxlbrhhqajaiozz))
                .thenReturn(Optional.of(returnedUrl));
        return caxlbrhhqajaiozz;
    }

    @Test
    void complete_JobWithNoTransfers_CreatesBlobWithEmptyResult() {
        // Act
        sut.complete(job);

        // Assert
        verify(blobStoreApi).putBlob(eq(storageAccountName), eq(containerName), eq(blobName), byteArrayCaptor.capture());
        assertThatJson(new String(byteArrayCaptor.getValue())).isEqualTo(new PartRelationshipsWithInfos());
    }

    @Test
    void complete_JobWithOneTransfer_CopiesBlob() {
        // Arrange
        job = job.toBuilder().completedTransfer(transfer).build();
        var bytes = faker.lorem().sentence().getBytes(StandardCharsets.UTF_8);
        when(blobStoreApi.getBlob(storageAccountName, containerName, blobName))
                .thenReturn(bytes);

        // Act
        sut.complete(job);

        // Assert
        verify(blobStoreApi).putBlob(storageAccountName, containerName, blobName, bytes);
    }
}
