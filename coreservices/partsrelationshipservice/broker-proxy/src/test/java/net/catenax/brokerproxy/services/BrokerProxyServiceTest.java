package net.catenax.brokerproxy.services;

import com.catenax.partsrelationshipservice.dtos.PartAspectUpdate;
import com.catenax.partsrelationshipservice.dtos.PartAttributeUpdate;
import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateListMessage;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import net.catenax.prs.testing.BrokerProxyDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.IntStream;

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
    MeterRegistry registry = new SimpleMeterRegistry();

    @Captor
    ArgumentCaptor<PartRelationshipUpdateListMessage> messageCaptor;

    @InjectMocks
    BrokerProxyService sut;

    BrokerProxyDtoMother generate = new BrokerProxyDtoMother();
    PartRelationshipUpdateList partRelationshipUpdateList = generate.partRelationshipUpdateList();
    PartAspectUpdate partAspectUpdate = generate.partAspectUpdate();
    PartAttributeUpdate partAttributeUpdate = generate.partAttributeUpdate();
    Faker faker = new Faker();

    @BeforeEach
    void setUp()
    {
        sut.initialize();
    }

    @Test
    void send_PartRelationshipUpdateList_sendsMessageToBroker() {
        // Act
        sut.send(partRelationshipUpdateList);

        // Assert
        verify(producerService).send(argThat(this::isExpectedBrokerMessage));
    }

    @Test
    void send_PartRelationshipUpdateList_generatesDistinctUUIDs() {
        // Arrange
        var nTimes = faker.number().numberBetween(2, 10);

        // Act
        IntStream.range(0, nTimes).forEach(i -> sut.send(partRelationshipUpdateList));

        // Assert
        verify(producerService, times(nTimes)).send(messageCaptor.capture());

        assertThat(messageCaptor.getAllValues().stream().map(v -> v.getPartRelationshipUpdateListId()))
                .doesNotHaveDuplicates();
    }

    @Test
    void send_PartRelationshipUpdateList_onProducerException_Throws() {
        // Arrange
        doThrow(new MessageProducerFailedException(new InterruptedException()))
                .when(producerService).send(any());

        // Act
        assertThatExceptionOfType(MessageProducerFailedException.class).isThrownBy(() ->
                sut.send(partRelationshipUpdateList));
    }

    @Test
    void send_PartAspectUpdate_sendsMessage() {
        // Act
        sut.send(partAspectUpdate);

        // Assert
        verify(producerService).send(partAspectUpdate);
    }

    @Test
    void send_PartAttributeUpdate_sendsMessage() {
        // Act
        sut.send(partAttributeUpdate);

        // Assert
        verify(producerService).send(partAttributeUpdate);
    }

    private boolean isExpectedBrokerMessage(PartRelationshipUpdateListMessage m) {
        assertThat(m.getPartRelationshipUpdateListId()).isNotNull();
        assertThat(m.getPayload()).isEqualTo(partRelationshipUpdateList);
        return true;
    }
}