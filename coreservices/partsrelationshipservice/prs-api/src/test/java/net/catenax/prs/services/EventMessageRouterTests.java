package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import net.catenax.prs.testing.PartUpdateEventMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventMessageRouterTests {

    @Mock
    PartRelationshipUpdateProcessor updateProcessor;
    @InjectMocks
    EventMessageRouter sut;

    PartUpdateEventMother generate = new PartUpdateEventMother();
    PartRelationshipUpdateEvent relationshipUpdate = generate.relationshipUpdateEvent();

    @Test
    void consumePartRelationshipUpdateEvent() {
        sut.route(relationshipUpdate);
        verify(updateProcessor).process(relationshipUpdate);
    }
}