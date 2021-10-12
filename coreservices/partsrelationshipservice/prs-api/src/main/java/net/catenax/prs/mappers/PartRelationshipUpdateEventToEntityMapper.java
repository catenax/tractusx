//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.mappers;

import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.entities.PartRelationshipEntityKey;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Mapper for {@link PartRelationshipUpdateEvent} update event object to {@link PartRelationshipEntity} entity.
 */
@Component
public class PartRelationshipUpdateEventToEntityMapper {
    /**
     * Map a {@link PartRelationshipUpdateEvent} event to {@link PartRelationshipEntity} entity.
     *
     * @param event see {@link PartRelationshipUpdateEvent}
     * @return List of {@link PartRelationshipEntity} containing data from update event.
     */
    public List<PartRelationshipEntity> toRelationships(final PartRelationshipUpdateEvent event) {
        final List<PartRelationshipEntity> relationshipEntityList = new ArrayList<>();
        final var updatedID = UUID.randomUUID();

        event.getRelationships().forEach(relInEvent -> {
            final var partRelationshipEntityKey = PartRelationshipEntityKey.builder()
                    .parentId(toPartIdEntityPart(relInEvent.getRelationship().getParent().getOneIDManufacturer(),
                            relInEvent.getRelationship().getParent().getObjectIDManufacturer()))
                    .childId(toPartIdEntityPart(relInEvent.getRelationship().getChild().getOneIDManufacturer(),
                            relInEvent.getRelationship().getChild().getObjectIDManufacturer()))
                    .effectTime(relInEvent.getEffectTime())
                    .removed(relInEvent.isRemove())
                    .lifeCycleStage(relInEvent.getStage())
                    .build();

            final var relationshipEntity = PartRelationshipEntity.builder()
                    .key(partRelationshipEntityKey)
                    .uploadDateTime(Instant.now())
                    .partRelationshipListId(updatedID)
                    .build();

            relationshipEntityList.add(relationshipEntity);

        });

        return relationshipEntityList;
    }


    private PartIdEntityPart toPartIdEntityPart(final String oneIDManufacturer, final String objectIDManufacturer) {
        return PartIdEntityPart.builder()
                .oneIDManufacturer(oneIDManufacturer)
                .objectIDManufacturer(objectIDManufacturer).build();
    }
}
