package net.catenax.brokerproxy.services;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateListMessage;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import net.catenax.brokerproxy.configuration.BrokerProxyConfiguration;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaOperations;

import java.time.Instant;
import java.util.stream.IntStream;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrokerProxyServiceTest {

    @Mock
    KafkaOperations<String, Object> kafka;

    @Spy
    MeterRegistry registry = new SimpleMeterRegistry();

    @Spy
    BrokerProxyConfiguration configuration = new BrokerProxyConfiguration();

    @Captor
    ArgumentCaptor<PartRelationshipUpdateListMessage> messageCaptor;

    @InjectMocks
    BrokerProxyService sut;

    DtoMother generate = new DtoMother();
    PartRelationshipUpdateList message = generate.partRelationshipUpdateList();
    Faker faker = new Faker();

    @BeforeEach
    void setUp()
    {
        sut.initialize();
        configuration.setKafkaTopic(faker.lorem().word());
    }

    @Test
    void uploadPartRelationshipUpdateList_sendsMessageToBroker() {
        // Arrange
        var topic = configuration.getKafkaTopic();

        // Act
        sut.uploadPartRelationshipUpdateList(message);

        // Assert
        verify(kafka).send(eq(topic), argThat(this::isExpectedBrokerMessage));
    }

    @Test
    void uploadPartRelationshipUpdateList_generatesDistinctUUIDs() {
        // Arrange
        var nTimes = faker.number().numberBetween(2, 10);

        // Act
        IntStream.range(0, nTimes).forEach(i -> sut.uploadPartRelationshipUpdateList(message));

        // Assert
        verify(kafka, times(nTimes)).send(anyString(), messageCaptor.capture());

        assertThat(messageCaptor.getAllValues().stream().map(v -> v.getPartRelationshipUpdateListId()))
                .doesNotHaveDuplicates();
    }

    private boolean isExpectedBrokerMessage(PartRelationshipUpdateListMessage m) {
        assertThat(m.getPartRelationshipUpdateListId()).isNotNull();
        assertThat(m.getPayload()).isEqualTo(message);
        return true;
    }
}