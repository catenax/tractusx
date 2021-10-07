//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.entities.PartAspectEntity;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartRelationshipEntityKey;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.repositories.PartAspectRepository;
import net.catenax.prs.repositories.PartAttributeRepository;
import net.catenax.prs.repositories.PartRelationshipRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

//import java.time.Instant;

/**
 * Service for processing parts tree update events.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartsTreeUpdateProcessorService {

    /**
     * Repository for retrieving {@link PartRelationshipEntity} data.
     */
    private final PartRelationshipRepository relationshipRepository;
    /**
     * Repository for retrieving {@link PartAspectEntity} data.
     */
    private final PartAspectRepository aspectRepository;
    /**
     * Repository for retrieving {@link PartAttributeEntity} data.
     */
    private final PartAttributeRepository attributeRepository;

    /**
     * Update {@link PartRelationshipUpdateEvent} data into database.
     * @param event Parts relationship update event from broker.
     */
    public void update(final PartRelationshipUpdateEvent event) {
        //Exception handle duplicate or retry for kafka consumer.
        // Also take care of duplicate/optimistic concurrency.
        var updatedID = UUID.randomUUID();

        for (var relInEvent : event.getRelationships()) {

            var partRelationshipEntityKey = PartRelationshipEntityKey.builder()
                    .parentId(toPartIdEntityPart(relInEvent.getRelationship().getParent().getOneIDManufacturer(),
                            relInEvent.getRelationship().getParent().getObjectIDManufacturer()))
                    .childId(toPartIdEntityPart(relInEvent.getRelationship().getChild().getOneIDManufacturer(),
                            relInEvent.getRelationship().getChild().getObjectIDManufacturer()))
                    .effectTime(relInEvent.getEffectTime())
                    .removed(relInEvent.isRemove())
                    .lifeCycleStage(relInEvent.getStage())
                    .partRelationshipListId(updatedID)
                    .build();

            var relationshipEntity = PartRelationshipEntity.builder()
                    .key(partRelationshipEntityKey)
                    .uploadDateTime(Instant.now())
                    .build();

            relationshipRepository.save(relationshipEntity);
        }

    }

    /**
     * Update {@link PartAttributeUpdateEvent} data into database.
     * @param event Parts attribute update event from broker.
     */
    public void update(final PartAttributeUpdateEvent event) {
        //Compare effect Time and decide insert/update/discard event.
        // Also take care of duplicate/optimistic concurrency.
    }

    /**
     * Update {@link PartAspectUpdateEvent} data into database.
     * @param event Parts aspect update event from broker.
     */
    public void update(final PartAspectUpdateEvent event) {
        //Compare effect Time and decide insert/update/discard event.
        // Also take care of duplicate/optimistic concurrency.
    }

    private PartIdEntityPart toPartIdEntityPart(String oneIDManufacturer, String objectIDManufacturer) {
        return PartIdEntityPart.builder()
                .oneIDManufacturer(oneIDManufacturer)
                .objectIDManufacturer(objectIDManufacturer).build();
    }

}
