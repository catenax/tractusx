package net.catenax.prs.connector.consumer.service;

import com.github.javafaker.Faker;
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.connector.util.JsonUtil;
import org.eclipse.dataspaceconnector.common.azure.BlobStoreApi;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static net.catenax.prs.connector.constants.PrsConnectorConstants.DATA_REQUEST_PRS_DESTINATION_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartsTreeRecursiveLogicTest {

    final RequestMother generate = new RequestMother();
    PartsTreeByObjectIdRequest request = generate.request();
    FileRequest fileRequest = FileRequest.builder().partsTreeRequest(request).build();
    PartId partId = toPartId(request);
    Faker faker = new Faker();
    Monitor monitor = new ConsoleMonitor();
    JsonUtil jsonUtil = new JsonUtil(monitor);
    String storageAccountName = faker.lorem().characters();
    String containerName = faker.lorem().word();
    String blobName = faker.lorem().word();
    String rootQueryConnectorAddress = faker.internet().url();
    PartsTreeRecursiveLogic sut;
    @Mock
    BlobStoreApi blobStoreApi;
    @Mock
    DataRequestFactory dataRequestFactory;
    @Mock
    PartsTreesAssembler assembler;
    @Mock
    DataRequest dataRequest;
    @Captor
    ArgumentCaptor<Stream<PartRelationshipsWithInfos>> partsTreesCaptor;
    @Captor
    ArgumentCaptor<Stream<PartId>> partIdsCaptor;

    @BeforeEach
    public void setUp() {
        sut = new PartsTreeRecursiveLogic(monitor, blobStoreApi, jsonUtil, dataRequestFactory, assembler);
    }

    @Test
    void initiate_WhenNoDataRequest_ReturnsEmptyStream() {
        // Arrange
        when(dataRequestFactory.createRequests(eq(fileRequest), isNull(), partIdsCaptor.capture()))
                .thenReturn(Stream.empty());

        // Act
        var result = sut.initiate(fileRequest);

        // Assert
        assertThat(result).isEmpty();
        assertThat(partIdsCaptor.getValue()).containsExactly(partId);
    }

    @Test
    void initiate_WhenDataRequest_ReturnsStream() {
        // Arrange
        when(dataRequestFactory.createRequests(eq(fileRequest), isNull(), partIdsCaptor.capture()))
                .thenReturn(Stream.of(dataRequest));

        // Act
        var result = sut.initiate(fileRequest);

        // Assert
        assertThat(result).containsExactly(dataRequest);
        assertThat(partIdsCaptor.getValue()).containsExactly(partId);
    }

    @Test
    void recurse() {
        // Arrange
        var transfer = transferProcess(blobName);
        PartRelationshipsWithInfos empty = generatePrsOutput();
        when(blobStoreApi.getBlob(storageAccountName, containerName, blobName))
                .thenReturn(serialize(empty));

        // Act
        var result = sut.recurse(transfer, fileRequest);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void complete_WithNoInput() {
        // Arrange
        PartRelationshipsWithInfos prsOutput = generatePrsOutput();
        when(assembler.assemblePartsTrees(partsTreesCaptor.capture()))
                .thenReturn(prsOutput);

        // Act
        sut.complete(List.of(), storageAccountName, containerName, blobName);

        // Assert
        assertThat(partsTreesCaptor.getValue()).isEmpty();
        verify(blobStoreApi).putBlob(storageAccountName, containerName, blobName, serialize(prsOutput));
    }

    private byte[] serialize(PartRelationshipsWithInfos prsOutput) {
        return jsonUtil.asString(prsOutput).getBytes(StandardCharsets.UTF_8);
    }

    @Test
    void complete_WithInput() {
        // Arrange
        var blob1 = faker.lorem().characters();
        var blob2 = faker.lorem().characters();
        var blob3 = faker.lorem().characters();
        var transfer1 = transferProcess(blob1);
        var transfer2 = transferProcess(blob2);
        var transfer3 = transferProcess(blob3);
        PartRelationshipsWithInfos prsOutput1 = generatePrsOutput();
        PartRelationshipsWithInfos prsOutput2 = generatePrsOutput();
        PartRelationshipsWithInfos prsOutput3 = generatePrsOutput();
        PartRelationshipsWithInfos prsOutput4 = generatePrsOutput();
        when(assembler.assemblePartsTrees(partsTreesCaptor.capture()))
                .thenReturn(prsOutput4);
        when(blobStoreApi.getBlob(storageAccountName, containerName, blob1))
                .thenReturn(serialize(prsOutput1));
        when(blobStoreApi.getBlob(storageAccountName, containerName, blob2))
                .thenReturn(serialize(prsOutput2));
        when(blobStoreApi.getBlob(storageAccountName, containerName, blob3))
                .thenReturn(serialize(prsOutput3));

        // Act
        sut.complete(List.of(transfer1, transfer2, transfer3), storageAccountName, containerName, blobName);

        // Assert
        assertThat(partsTreesCaptor.getValue()).containsExactly(prsOutput1, prsOutput2, prsOutput3);
        verify(blobStoreApi).putBlob(storageAccountName, containerName, blobName, serialize(prsOutput4));
    }

    private TransferProcess transferProcess(String blobName) {
        return
                TransferProcess.Builder.newInstance()
                        .id(faker.lorem().characters())
                        .dataRequest(DataRequest.Builder.newInstance()
                                .connectorAddress(rootQueryConnectorAddress)
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
    }

    private PartId toPartId(PartsTreeByObjectIdRequest partsTreeRequest) {
        var partId = new PartId();
        partId.setOneIDManufacturer(partsTreeRequest.getOneIDManufacturer());
        partId.setObjectIDManufacturer(partsTreeRequest.getObjectIDManufacturer());
        return partId;
    }

    private PartRelationshipsWithInfos generatePrsOutput() {
        return generate.prsOutput()
                .addRelationshipsItem(generate.relationship())
                .addPartInfosItem(generate.partInfo());
    }
}