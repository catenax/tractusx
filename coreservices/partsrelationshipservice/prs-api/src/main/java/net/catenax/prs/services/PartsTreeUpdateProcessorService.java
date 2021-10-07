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
import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;
import net.catenax.prs.entities.PartAspectEntity;
import net.catenax.prs.entities.PartAspectEntityKey;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartAttributeEntityKey;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.entities.PartRelationshipEntityKey;
import net.catenax.prs.repositories.PartAspectRepository;
import net.catenax.prs.repositories.PartAttributeRepository;
import net.catenax.prs.repositories.PartRelationshipRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


/**
 * Service for processing parts tree update events.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.CommentSize"})
//TODO: Include it in code coverage.
@ExcludeFromCodeCoverageGeneratedReport
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
     *
     * @param event Parts relationship update event from broker.
     */
    public void update(final PartRelationshipUpdateEvent event) {
        final var updatedID = UUID.randomUUID();

        //TODO: Handle Exception.

        event.getRelationships()
                .forEach(relInEvent -> {
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
                    /*
                      NOTE: This findById approach works here as we only have single writer to db.
                      This is done to keep it simple for speedboat point of view. If we have possibility of parallel writers then a race condition may occur.
                     */
                    if (relationshipRepository.findById(partRelationshipEntityKey).isEmpty()) {
                        relationshipRepository.save(relationshipEntity);
                    }
                });
    }

    /**
     * Update {@link PartAttributeUpdateEvent} data into database.
     *
     * @param event Parts attribute update event from broker.
     */
    public void update(final PartAttributeUpdateEvent event) {

        final var partAttributeEntityKey = PartAttributeEntityKey.builder()
                .attribute(event.getName().name())
                .partId(toPartIdEntityPart(event.getPart().getOneIDManufacturer(), event.getPart().getObjectIDManufacturer()))
                .build();

        final var partAttributeEntity = PartAttributeEntity.builder()
                .key(partAttributeEntityKey)
                .value(event.getValue())
                .effectTime(event.getEffectTime())
                .lastModifiedTime(Instant.now())
                .build();

                    /*
                      NOTE: This findById approach works here as we only have single writer to db.
                      This is done to keep it simple for speedboat point of view. If we have possibility of parallel writers then a race condition may occur.
                     */
        final var partAttribute = attributeRepository.findById(partAttributeEntityKey);

        if (partAttribute.isEmpty() || partAttribute.get().getEffectTime().isBefore(event.getEffectTime())) {
            attributeRepository.save(partAttributeEntity);
        }
    }

    /**
     * Update {@link PartAspectUpdateEvent} data into database.
     *
     * @param event Parts aspect update event from broker.
     */
    public void update(final PartAspectUpdateEvent event) {
        //NOTE: Removal is out of scope for speedboat.

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

                    /*
                      NOTE: This findById approach works here as we only have single writer to db.
                      This is done to keep it simple for speedboat point of view. If we have possibility of parallel writers then a race condition may occur.
                     */
            final var partAspect = aspectRepository.findById(partAspectEntityKey);

            if (partAspect.isEmpty() || partAspect.get().getEffectTime().isBefore(event.getEffectTime())) {
                aspectRepository.save(partAspectEntity);
            }

        });

    }

    private PartIdEntityPart toPartIdEntityPart(final String oneIDManufacturer, final String objectIDManufacturer) {
        return PartIdEntityPart.builder()
                .oneIDManufacturer(oneIDManufacturer)
                .objectIDManufacturer(objectIDManufacturer).build();
    }

}
