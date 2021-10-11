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

import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import lombok.RequiredArgsConstructor;
import net.catenax.prs.entities.PartAspectEntity;
import net.catenax.prs.entities.PartAspectEntityKey;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartAttributeEntityKey;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.entities.PartRelationshipEntityKey;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Mapper for Parts update events to db entities.
 */
@Component
@RequiredArgsConstructor
public class PartUpdateEventToEntityMapper {

    /**
     * Map a {@link PartRelationshipUpdateEvent} event to {@link PartRelationshipEntity} entity.
     *
     * @param event see {@link PartRelationshipUpdateEvent}
     * @return List of {@link PartRelationshipEntity}
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

    /**
     * Map a {@link PartAttributeUpdateEvent} event to {@link PartAttributeEntity} entity.
     *
     * @param event see {@link PartAttributeUpdateEvent}
     * @return List of {@link PartAttributeEntity}
     */
    public PartAttributeEntity toAttribute(final PartAttributeUpdateEvent event) {
        final var partAttributeEntityKey = PartAttributeEntityKey.builder()
                .attribute(event.getName().name())
                .partId(toPartIdEntityPart(event.getPart().getOneIDManufacturer(), event.getPart().getObjectIDManufacturer()))
                .build();

        return PartAttributeEntity.builder()
                .key(partAttributeEntityKey)
                .value(event.getValue())
                .effectTime(event.getEffectTime())
                .lastModifiedTime(Instant.now())
                .build();
    }

    /**
     * Map a {@link PartAspectUpdateEvent} event to {@link PartAspectEntity} entity.
     *
     * @param event see {@link PartAspectUpdateEvent}
     * @return List of {@link PartAspectEntity}
     */
    public List<PartAspectEntity> toAspects(final PartAspectUpdateEvent event) {
        final List<PartAspectEntity> aspectEntityList = new ArrayList<>();

        event.getAspects().forEach(aspectInEvent -> {
            final var partAspectEntityKey = PartAspectEntityKey.builder()
                    .partId(toPartIdEntityPart(event.getPart().getOneIDManufacturer(), event.getPart().getObjectIDManufacturer()))
                    .name(aspectInEvent.getName())
                    .build();

            final var partAspectEntity = PartAspectEntity.builder()
                    .key(partAspectEntityKey)
                    .effectTime(event.getEffectTime())
                    .url(aspectInEvent.getUrl())
                    .lastModifiedTime(Instant.now())
                    .build();

            aspectEntityList.add(partAspectEntity);
        });

        return aspectEntityList;
    }

    private PartIdEntityPart toPartIdEntityPart(final String oneIDManufacturer, final String objectIDManufacturer) {
        return PartIdEntityPart.builder()
                .oneIDManufacturer(oneIDManufacturer)
                .objectIDManufacturer(objectIDManufacturer).build();
    }

}
