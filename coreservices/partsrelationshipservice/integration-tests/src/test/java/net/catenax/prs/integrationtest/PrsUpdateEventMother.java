//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.integrationtest;

import com.catenax.partsrelationshipservice.dtos.PartId;
import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import net.catenax.prs.testing.DtoMother;

import java.time.Instant;

/**
 * Object Mother to generate data for integration tests.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">
 * https://martinfowler.com/bliki/ObjectMother.html</a>
 */
public class PrsUpdateEventMother {

    /**
     * Dto mother object that generates test data.
     */
    private final DtoMother generate = new DtoMother();


    /**
     * Generate a {@link PartRelationshipUpdateEvent.RelationshipUpdate} with sample data.
     * @return see {@link PartRelationshipUpdateEvent.RelationshipUpdate}.
     */
    protected PartRelationshipUpdateEvent.RelationshipUpdate sampleRelationshipUpdate() {
        return PartRelationshipUpdateEvent.RelationshipUpdate.builder()
                .withEffectTime(Instant.now())
                .withRemove(false)
                .withStage(PartLifecycleStage.BUILD)
                .withRelationship(generate.partRelationship())
                .build();
    }

    /**
     * Generate a {@link PartRelationshipUpdateEvent.RelationshipUpdate} with sample data.
     * @param effectTime effect effectTime.
     * @return see {@link PartRelationshipUpdateEvent.RelationshipUpdate}.
     */
    protected PartRelationshipUpdateEvent.RelationshipUpdate sampleRelationshipUpdate(Instant effectTime) {
        return sampleRelationshipUpdate()
                .toBuilder()
                .withEffectTime(effectTime)
                .build();
    }

    /**
     * Generate a {@link PartRelationshipUpdateEvent.RelationshipUpdate} with sample data.
     * @param parent parent.
     * @return see {@link PartRelationshipUpdateEvent.RelationshipUpdate}.
     */
    public PartRelationshipUpdateEvent.RelationshipUpdate sampleRelationshipWithParent(PartId parent) {
        return sampleRelationshipUpdate()
                .toBuilder()
                .withRelationship(generate.partRelationship().toBuilder().withParent(parent).build())
                .build();
    }
}
