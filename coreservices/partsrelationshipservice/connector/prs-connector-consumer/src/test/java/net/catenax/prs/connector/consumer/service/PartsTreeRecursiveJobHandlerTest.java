package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartRelationship;
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
import java.util.stream.Collectors;

import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_REQUEST_PARAMETERS;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.PRS_REQUEST_ASSET_ID;
import static net.catenax.prs.connector.constants.PrsConnectorConstants.PRS_REQUEST_POLICY_ID;
import static net.catenax.prs.connector.consumer.service.ConsumerService.CONTAINER_NAME_KEY;
import static net.catenax.prs.connector.consumer.service.ConsumerService.DESTINATION_PATH_KEY;
import static net.catenax.prs.connector.consumer.service.PartsTreeRecursiveJobHandler.BLOB_NAME;
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
    PartRelationship parentChildRelationship = generate.relationship();
    Faker faker = new Faker();
    Monitor monitor = new ConsoleMonitor();
    String storageAccountName = faker.lorem().characters();
    String containerName = faker.lorem().word();
    String blobName = faker.lorem().word();
    String rootQueryConnectorAddress = faker.internet().url();
    String anotherConnectorAddress = faker.internet().url();
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
    TransferProcess transferWithResources = TransferProcess.Builder.newInstance()
            .id(faker.lorem().characters())
            .dataRequest(DataRequest.Builder.newInstance()
                    .connectorAddress(rootQueryConnectorAddress)
                    .dataDestination(DataAddress.Builder.newInstance()
                            .type(AzureBlobStoreSchema.TYPE)
                            .property(AzureBlobStoreSchema.ACCOUNT_NAME, storageAccountName)
                            .property(AzureBlobStoreSchema.CONTAINER_NAME, containerName)
                            .build())
                    .properties(Map.of(
                            DATA_REQUEST_PRS_DESTINATION_PATH, BLOB_NAME
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
    }

    @Test
    void initiate_WhenRegistryMatches_ReturnsOneDataRequest() throws JsonProcessingException {
        // Arrange
        when(registryClient.getUrl(toPartId(fileRequest.getPartsTreeRequest())))
                .thenReturn(Optional.of(rootQueryConnectorAddress));

        // Act
        var result = sut.initiate(job);

        // Assert
        var resultAsList = result.collect(Collectors.toList());
        assertThat(resultAsList).hasSize(1);

        // Verify that initiateConsumerRequest got called with correct DataRequest input.

        assertDataRequest(resultAsList.get(0), rootQueryConnectorAddress, fileRequest.getPartsTreeRequest());
    }

    @Test
    void initiate_WhenRegistryNoMatch_ReturnsEmpty() {
        // Act
        var result = sut.initiate(job);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse_WhenRegistryNoMatch_ReturnsEmpty() throws Exception {
        // Arrange
        setUpStorageToReturnPartsTreeAndRegistryToReturn(Optional.empty());

        // Act
        var result = sut.recurse(job, transferWithResources);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse_WhenRegistryMatchesAndUrlUnchanged_ReturnsEmpty() throws Exception {
        // Arrange
        setUpStorageToReturnPartsTreeAndRegistryToReturn(Optional.of(rootQueryConnectorAddress));

        // Act
        var result = sut.recurse(job, transferWithResources);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse_WhenRegistryMatchesAndUrlChanged_ReturnsOneDataRequest() throws Exception {
        // Arrange
        setUpStorageToReturnPartsTreeAndRegistryToReturn(Optional.of(anotherConnectorAddress));

        // Act
        var result = sut.recurse(job, transferWithResources);

        // Assert
        var resultAsList = result.collect(Collectors.toList());
        assertThat(resultAsList).hasSize(1);

        // Verify that initiateConsumerRequest got called with correct DataRequest input.

        var c = parentChildRelationship.getChild(); // TODO coupling
        PartsTreeByObjectIdRequest r = fileRequest.getPartsTreeRequest()
                .toBuilder()
                .oneIDManufacturer(c.getOneIDManufacturer())
                .objectIDManufacturer(c.getObjectIDManufacturer())
                .build();
        assertDataRequest(resultAsList.get(0), anotherConnectorAddress, r);
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
        job = job.toBuilder().completedTransfer(transferWithResources).build();
        var bytes = faker.lorem().sentence().getBytes(StandardCharsets.UTF_8);
        when(blobStoreApi.getBlob(storageAccountName, containerName, BLOB_NAME))
                .thenReturn(bytes);

        // Act
        sut.complete(job);

        // Assert
        verify(blobStoreApi).putBlob(storageAccountName, containerName, blobName, bytes);
    }

    private PartId toPartId(PartsTreeByObjectIdRequest partsTreeRequest) {
        var partId = new PartId();
        partId.setOneIDManufacturer(partsTreeRequest.getOneIDManufacturer());
        partId.setObjectIDManufacturer(partsTreeRequest.getObjectIDManufacturer());
        return partId;
    }

    private void setUpStorageToReturnPartsTreeAndRegistryToReturn(Optional<String> connectorAddress) throws JsonProcessingException {
        var partialPartsTree = generate.prsOutput(parentChildRelationship);
        var bytes = MAPPER.writeValueAsBytes(partialPartsTree);
        when(blobStoreApi.getBlob(storageAccountName, containerName, BLOB_NAME))
                .thenReturn(bytes);
        when(registryClient.getUrl(parentChildRelationship.getChild()))
                .thenReturn(connectorAddress);
    }

    private void assertDataRequest(DataRequest actualRequest, String connectorAddress, PartsTreeByObjectIdRequest partsTreeRequest) throws JsonProcessingException {
        String serializedPrsRequest = MAPPER.writeValueAsString(partsTreeRequest);
        var expectedRequest = DataRequest.Builder.newInstance()
                .id(actualRequest.getId())
                .connectorAddress(connectorAddress)
                .processId(null)
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
                        DATA_REQUEST_PRS_DESTINATION_PATH, BLOB_NAME
                ))
                .managedResources(true)
                .build();
        assertThat(actualRequest)
                .usingRecursiveComparison()
                .isEqualTo(expectedRequest);
    }
}
