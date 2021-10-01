package net.catenax.brokerproxy.services;

import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import net.catenax.brokerproxy.BrokerProxyDtoMother;
import net.catenax.brokerproxy.configuration.BrokerProxyConfiguration;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import net.catenax.brokerproxy.requests.PartAspectUpdateRequest;
import net.catenax.brokerproxy.requests.PartAttributeUpdateRequest;
import net.catenax.brokerproxy.requests.PartRelationshipUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrokerProxyServiceTest {

    @Mock
    MessageProducerService producerService;

    @Spy
    BrokerProxyConfiguration configuration;

    @Spy
    MeterRegistry registry = new SimpleMeterRegistry();

    @InjectMocks
    BrokerProxyService sut;

    BrokerProxyDtoMother generate = new BrokerProxyDtoMother();
    PartRelationshipUpdateRequest partRelationshipUpdateRequest = generate.partRelationshipUpdateList();
    PartAspectUpdateRequest partAspectUpdateRequest = generate.partAspectUpdate();
    PartAttributeUpdateRequest partAttributeUpdateRequest = generate.partAttributeUpdate();
    Faker faker = new Faker();

    @BeforeEach
    void setUp()
    {
        sut.initialize();
        configuration.setKafkaTopicRelationships(faker.lorem().word());
        configuration.setKafkaTopicAspects(faker.lorem().word());
        configuration.setKafkaTopicAttributes(faker.lorem().word());
    }

    @Test
    void send_PartRelationshipUpdateList_sendsMessageToBroker() {
        // Act
        sut.send(partRelationshipUpdateRequest);

        // Assert
        verify(producerService).send(any(String.class), argThat(this::isExpectedBrokerMessageForRelationshipUpdate));
    }

    @Test
    void send_PartRelationshipUpdateList_onProducerException_Throws() {
        // Arrange
        doThrow(new MessageProducerFailedException(new InterruptedException()))
                .when(producerService).send(any(), any());

        // Act
        assertThatExceptionOfType(MessageProducerFailedException.class).isThrownBy(() ->
                sut.send(partRelationshipUpdateRequest));
    }

    @Test
    void send_PartAspectUpdate_sendsMessage() {
        // Act
        sut.send(partAspectUpdateRequest);

        // Assert
        verify(producerService).send(any(String.class), argThat(this::isExpectedBrokerMessageForAspectUpdate));
    }

    @Test
    void send_PartAttributeUpdate_sendsMessage() {
        // Act
        sut.send(partAttributeUpdateRequest);

        // Assert
        verify(producerService).send(any(String.class), argThat(this::isExpectedBrokerMessageForAttributeUpdate));
    }

    private boolean isExpectedBrokerMessageForRelationshipUpdate(PartRelationshipUpdateEvent event) {
        assertThat(event.getRelationships()).isNotEmpty();
        var eventData = event.getRelationships().get(0);
        assertThat(eventData.getRelationship()).isEqualTo(partRelationshipUpdateRequest.getRelationships().get(0).getRelationship());
        assertThat(eventData.getEffectTime()).isEqualTo(partRelationshipUpdateRequest.getRelationships().get(0).getEffectTime());
        assertThat(eventData.getStage()).isEqualTo(partRelationshipUpdateRequest.getRelationships().get(0).getStage());
        assertThat(eventData.isRemove()).isFalse();
        return true;
    }

    private boolean isExpectedBrokerMessageForAspectUpdate(PartAspectUpdateEvent event) {
        assertThat(event.getAspects()).isEqualTo(partAspectUpdateRequest.getAspects());
        assertThat(event.getPart()).isEqualTo(partAspectUpdateRequest.getPart());
        assertThat(event.getEffectTime()).isEqualTo(partAspectUpdateRequest.getEffectTime());
        assertThat(event.isRemove()).isFalse();
        return true;
    }

    private boolean isExpectedBrokerMessageForAttributeUpdate(PartAttributeUpdateEvent event) {
        assertThat(event.getPart()).isEqualTo(partAttributeUpdateRequest.getPart());
        assertThat(event.getEffectTime()).isEqualTo(partAttributeUpdateRequest.getEffectTime());
        assertThat(event.getName()).isEqualTo(partAttributeUpdateRequest.getName());
        assertThat(event.getValue()).isEqualTo(partAttributeUpdateRequest.getValue());
        return true;
    }
}