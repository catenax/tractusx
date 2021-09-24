package net.catenax.brokerproxy.services;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateListMessage;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import net.catenax.brokerproxy.configuration.BrokerProxyConfiguration;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        when(kafka.send(any(), any())).thenReturn(AsyncResult.forValue(null));
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

    @Test
    void uploadPartRelationshipUpdateList_onKafkaFailure_Throws() {
        // Arrange
        verifyExceptionThrownOnFailure(new ExecutionException(new IOException()));
    }

    @Test
    void uploadPartRelationshipUpdateList_onInterrupted_Throws() {
        // Arrange
        verifyExceptionThrownOnFailure(new InterruptedException());
    }

    private void verifyExceptionThrownOnFailure(Throwable exception) {
        when(kafka.send(any(), any())).thenReturn(AsyncResult.forExecutionException(exception));

        // Act
        assertThatExceptionOfType(MessageProducerFailedException.class).isThrownBy(() ->
                sut.uploadPartRelationshipUpdateList(message));
    }

    private boolean isExpectedBrokerMessage(PartRelationshipUpdateListMessage m) {
        assertThat(m.getPartRelationshipUpdateListId()).isNotNull();
        assertThat(m.getPayload()).isEqualTo(message);
        return true;
    }
}