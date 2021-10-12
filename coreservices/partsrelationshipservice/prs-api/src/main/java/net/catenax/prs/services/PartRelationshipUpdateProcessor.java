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

import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.mappers.PartRelationshipUpdateEventToEntityMapper;
import net.catenax.prs.repositories.PartRelationshipRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


/**
 * Service for processing parts tree update events.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartRelationshipUpdateProcessor {

    /**
     * Repository for retrieving {@link PartRelationshipEntity} data.
     */
    private final PartRelationshipRepository relationshipRepository;

    /**
     * Mapper for Parts relationship events to db entity.
     */
    private final PartRelationshipUpdateEventToEntityMapper entityMapper;

    /**
     * Update {@link PartRelationshipUpdateEvent} data into database.
     *
     * @param event Parts relationship update event from broker.
     */
    public void process(final PartRelationshipUpdateEvent event) {
        entityMapper.toRelationships(event)
                .forEach(partRelationshipEntity -> {
                    try {
                        persistIfNew(partRelationshipEntity);
                    } catch (DataIntegrityViolationException e) {
                        log.warn("Failed to persist entity, probably because an entity with same primary key was concurrently inserted. Trying again.", e);
                        persistIfNew(partRelationshipEntity);
                    }
                });
    }

    private void persistIfNew(PartRelationshipEntity partRelationshipEntity) {
        if (relationshipRepository.findById(partRelationshipEntity.getKey()).isEmpty()) {
            relationshipRepository.saveAndFlush(partRelationshipEntity);
        } else {
            log.info("Ignoring duplicate entity");
        }
    }
}
