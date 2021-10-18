package net.catenax.prs.services;

import com.github.javafaker.Faker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import net.catenax.prs.dtos.events.PartRelationshipsUpdateRequest;
import net.catenax.prs.testing.UpdateRequestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventMessageRouterTests {

    @Mock
    PartRelationshipUpdateProcessor updateProcessor;
    @Spy
    MeterRegistry registry = new SimpleMeterRegistry();
    @InjectMocks
    EventMessageRouter sut;

    Faker faker = new Faker();
    UpdateRequestMother generate = new UpdateRequestMother();
    PartRelationshipsUpdateRequest relationshipUpdate = generate.partRelationshipUpdateList();
    Instant timestamp = faker.date().past(10, DAYS).toInstant();

    @BeforeEach
    void setUp() {
        sut.initialize();
    }

    @Test
    void consumePartRelationshipUpdateEvent() {
        // Act
        sut.route(relationshipUpdate, timestamp.toEpochMilli());

        // Assert
        verify(updateProcessor).process(relationshipUpdate, timestamp);
    }

    @Test
    void consumePartRelationshipUpdateEvent_ProducesMetric_MessageProcessingTime() {
        // Act
        sut.route(relationshipUpdate, timestamp.toEpochMilli());

        // Assert
        var timer = this.registry.timer("message_processing_time");
        assertThat(timer.count()).isEqualTo(1);
    }
    @Test
    void consumePartRelationshipUpdateEvent_ProducesMetric_MessageAge() {
        // Act
        var before = Instant.now();
        sut.route(relationshipUpdate, timestamp.toEpochMilli());
        var after = Instant.now();

        // Assert
        var timer = this.registry.timer("message_age");
        assertThat(timer.count()).isEqualTo(1);
        assertThat(Duration.ofMillis((long) timer.totalTime(MILLISECONDS))).isBetween(
                Duration.between(timestamp, before),
                Duration.between(timestamp, after));
    }
}
