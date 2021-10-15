package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipsUpdateRequest;
import net.catenax.prs.testing.UpdateRequestMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventMessageRouterTests {

    @Mock
    PartRelationshipUpdateProcessor updateProcessor;
    @InjectMocks
    EventMessageRouter sut;

    UpdateRequestMother generate = new UpdateRequestMother();
    PartRelationshipsUpdateRequest relationshipUpdate = generate.partRelationshipUpdateList();
    Long epochTimeMilli = Instant.now().toEpochMilli();

    @Test
    void consumePartRelationshipUpdateEvent() {
        // Act
        sut.route(relationshipUpdate, epochTimeMilli);

        // Assert
        verify(updateProcessor).process(relationshipUpdate, Instant.ofEpochMilli(epochTimeMilli));
    }
}
