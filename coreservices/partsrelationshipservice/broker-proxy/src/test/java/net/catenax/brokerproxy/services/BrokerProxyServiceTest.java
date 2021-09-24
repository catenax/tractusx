package net.catenax.brokerproxy.services;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateListMessage;
import com.github.javafaker.Faker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    DtoMother generate = new DtoMother();
    PartRelationshipUpdateList message = generate.partRelationshipUpdateList();
    Faker faker = new Faker();

    @BeforeEach
    void setUp()
    {
        sut.initialize();
    }

    @Test
    void sendPartRelationshipUpdateList_sendsMessageToBroker() {
        // Act
        sut.send(message);

        // Assert
        verify(producerService).send(argThat(this::isExpectedBrokerMessage));
    }

    @Test
    void sendPartRelationshipUpdateList_generatesDistinctUUIDs() {
        // Arrange
        var nTimes = faker.number().numberBetween(2, 10);

        // Act
        IntStream.range(0, nTimes).forEach(i -> sut.send(message));

        // Assert
        verify(producerService, times(nTimes)).send(messageCaptor.capture());

        assertThat(messageCaptor.getAllValues().stream().map(v -> v.getPartRelationshipUpdateListId()))
                .doesNotHaveDuplicates();
    }

    @Test
    void sendPartRelationshipUpdateList_onProducerException_Throws() {
        // Arrange
        doThrow(new MessageProducerFailedException(new InterruptedException()))
                .when(producerService).send(any());

        // Act
        assertThatExceptionOfType(MessageProducerFailedException.class).isThrownBy(() ->
                sut.send(message));
    }

    private boolean isExpectedBrokerMessage(PartRelationshipUpdateListMessage m) {
        assertThat(m.getPartRelationshipUpdateListId()).isNotNull();
        assertThat(m.getPayload()).isEqualTo(message);
        return true;
    }
}