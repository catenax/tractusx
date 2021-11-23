package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.connector.consumer.configuration.ConsumerConfiguration;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class PartsTreeRecursiveJobHandlerTest {

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
    TransferProcess transfer = TransferProcess.Builder.newInstance()
            .id(faker.lorem().characters())
            .build();
    private final RequestMother generate = new RequestMother();
    private final FileRequest fileRequest = generate.fileRequest();

    @BeforeEach
    public void setUp() {
        sut = new PartsTreeRecursiveJobHandler(monitor, configuration);
    }

    @Test
    void initiate() throws Exception {
        // Arrange
        String serializedRequest1 = MAPPER.writeValueAsString(fileRequest);
        String serializedRequest2 = MAPPER.writeValueAsString(fileRequest.getPartsTreeRequest());

        job = job.toBuilder().jobData(Map.of(ConsumerService.PARTS_REQUEST_KEY, serializedRequest1)).build();

        // Act
        var result = sut.initiate(job);

        // Assert
        var resultAsList = result.collect(Collectors.toList());
        assertThat(resultAsList).hasSize(1);

        // Verify that initiateConsumerRequest got called with correct DataRequest input.
        var expectedDataRequest = DataRequest.Builder.newInstance()
                .id(resultAsList.get(0).getId())
                .connectorAddress(fileRequest.getConnectorAddress())
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
                        "prs-request-parameters", serializedRequest2,
                        "prs-destination-path", fileRequest.getDestinationPath()
                ))
                .managedResources(true)
                .build();

        assertThat(resultAsList)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(expectedDataRequest);
    }

    @Test
    void recurse() {
        // Act
        var result = sut.recurse(job, transfer);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void complete() {
        // Act
        sut.complete(job);
    }
}