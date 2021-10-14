//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.testing;

import com.catenax.partsrelationshipservice.dtos.PartAttribute;
import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Object Mother to generate fake domain data for testing.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">
 *     https://martinfowler.com/bliki/ObjectMother.html</a>
 */
public class PartUpdateEventMother {

    /**
     * DTO mother object that generates test data.
     */
    private final transient DtoMother dtoMother = new DtoMother();

    /**
     * JavaFaker instance used to generate random data.
     */
    private final transient Faker faker = new Faker();

    /**
     * Generate a {@link PartRelationshipUpdateEvent} containing random data.
     *
     * @return see {@link PartRelationshipUpdateEvent}.
     */
    public PartRelationshipUpdateEvent relationshipUpdateEvent() {
        final var partRelationship = PartRelationshipUpdateEvent.RelationshipUpdate.builder()
                .withRelationship(dtoMother.partRelationship())
                .withRemove(faker.bool().bool())
                .withEffectTime(faker.date().past(faker.number().randomDigitNotZero(), TimeUnit.DAYS).toInstant())
                .withStage(faker.options().option(PartLifecycleStage.class))
                .build();

        return PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(partRelationship))
                .build();
    }

    /**
     * Generate a {@link PartAttributeUpdateEvent} containing random data.
     *
     * @return see {@link PartAttributeUpdateEvent}.
     */
    public PartAttributeUpdateEvent attributeUpdateEvent() {

        return PartAttributeUpdateEvent.builder()
                .withPart(dtoMother.partId())
                .withName(faker.options().option(PartAttribute.class))
                .withValue(faker.lorem().word())
                .withEffectTime(faker.date().past(faker.number().randomDigitNotZero(), TimeUnit.DAYS).toInstant())
                .build();
    }

    /**
     * Generate a {@link PartAspectUpdateEvent} containing random data.
     *
     * @return see {@link PartAspectUpdateEvent}.
     */
    public PartAspectUpdateEvent aspectUpdateEvent() {

        return PartAspectUpdateEvent.builder()
                .withPart(dtoMother.partId())
                .withAspects(List.of(dtoMother.partAspect()))
                .withRemove(faker.bool().bool())
                .withEffectTime(faker.date().past(faker.number().randomDigitNotZero(), TimeUnit.DAYS).toInstant())
                .build();
    }
}
