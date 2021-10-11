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
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.mappers.PartUpdateEventToEntityMapper;
import net.catenax.prs.repositories.PartAspectRepository;
import net.catenax.prs.repositories.PartAttributeRepository;
import net.catenax.prs.repositories.PartRelationshipRepository;
import org.springframework.stereotype.Service;


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
     * Mapper for Parts update events to db entities.
     */
    private final PartUpdateEventToEntityMapper entityMapper;

    /**
     * Update {@link PartRelationshipUpdateEvent} data into database.
     *
     * @param event Parts relationship update event from broker.
     */
    public void update(final PartRelationshipUpdateEvent event) {

        entityMapper.toRelationships(event)
                .forEach(partRelationshipEntity -> {
                     /*
                      NOTE: This findById approach works here as we only have single writer to db.
                      This is done to keep it simple for speedboat point of view. If we have possibility of parallel writers then a race condition may occur.
                     */
                    if (relationshipRepository.findById(partRelationshipEntity.getKey()).isEmpty()) {
                        relationshipRepository.save(partRelationshipEntity);
                    }
                });
    }

    /**
     * Update {@link PartAttributeUpdateEvent} data into database.
     *
     * @param event Parts attribute update event from broker.
     */
    public void update(final PartAttributeUpdateEvent event) {
        final var partAttributeEntity = entityMapper.toAttribute(event);
         /*
                      NOTE: This findById approach works here as we only have single writer to db.
                      This is done to keep it simple for speedboat point of view. If we have possibility of parallel writers then a race condition may occur.
                     */
        final var partAttribute = attributeRepository.findById(partAttributeEntity.getKey());

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
        //NOTE: Data deletion is out of scope for speedboat.

        entityMapper.toAspects(event)
                .forEach(partAspectEntity -> {
                    /*
                      NOTE: This findById approach works here as we only have single writer to db.
                      This is done to keep it simple for speedboat point of view. If we have possibility of parallel writers then a race condition may occur.
                     */
                    final var partAspect = aspectRepository.findById(partAspectEntity.getKey());
                    if (partAspect.isEmpty() || partAspect.get().getEffectTime().isBefore(partAspectEntity.getEffectTime())) {
                        aspectRepository.save(partAspectEntity);
                    }
                });
    }
}
