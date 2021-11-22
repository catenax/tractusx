package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
import net.catenax.prs.connector.consumer.registry.StubRegistryClient;
import net.catenax.prs.connector.job.JobState;
import net.catenax.prs.connector.job.MultiTransferJob;
import net.catenax.prs.connector.requests.FileRequest;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PartsTreeRecursiveJobHandlerTest {

    @Mock
    private StubRegistryClient registryClient;

    static final ObjectMapper MAPPER = new ObjectMapper();
    Faker faker = new Faker();
    Monitor monitor = new ConsoleMonitor();
    ConsumerConfiguration configuration = ConsumerConfiguration.builder()
            .storageAccountName(faker.lorem().characters())
            .build();
    PartsTreeRecursiveJobHandler sut;
    MultiTransferJob job = MultiTransferJob.builder()
            .jobId(faker.lorem().characters())
            .state(faker.options().option(JobState.class))
            .build();
    TransferProcess.Builder transferBuilder = TransferProcess.Builder.newInstance()
            .id(faker.lorem().characters());
    private final RequestMother generate = new RequestMother();
    private final FileRequest fileRequest = generate.fileRequest();
    String url = faker.internet().url();
    DataRequest.Builder requestBuilder;

    @BeforeEach
    public void setUp() throws Exception {
        sut = new PartsTreeRecursiveJobHandler(monitor, configuration, registryClient);
        var serializedFileRequest = MAPPER.writeValueAsString(fileRequest);
        job = job.toBuilder().jobData(Map.of(ConsumerService.PARTS_REQUEST_KEY, serializedFileRequest)).build();
        String serializedPrsRequest = MAPPER.writeValueAsString(fileRequest.getPartsTreeRequest());
        requestBuilder = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString())
                .connectorAddress(url)
                .protocol("ids-rest")
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance()
                        .id("prs-request")
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type(AzureBlobStoreSchema.TYPE)
                        .property("account", configuration.getStorageAccountName())
                        .build())
                .properties(Map.of(
                        "prs-request-parameters", serializedPrsRequest,
                        "prs-destination-path", fileRequest.getDestinationPath()
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
        assertThat(resultAsList)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedDataRequest);
    }

    @Test
    void initiate_WhenRegistryNoMatch_ReturnsEmpty() {
        // Act
        var result = sut.initiate(job);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void recurse() {
        // Act
        transferBuilder.dataRequest(requestBuilder.build());
        var result = sut.recurse(job, transferBuilder.build());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void complete() {
        // Act
        sut.complete(job);
    }
}