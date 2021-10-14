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
import com.catenax.partsrelationshipservice.dtos.events.PartAspectsUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.events.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipUpdate;
import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipsUpdateEvent;
import com.github.javafaker.Faker;

import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.DAYS;

/**
 * Object Mother to generate fake DTO data for testing.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">
 * https://martinfowler.com/bliki/ObjectMother.html</a>
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class EventMessageMother {
    /**
     * JavaFaker instance used to generate random data.
     */
    private final transient Faker faker = new Faker();
    /**
     * Object Mother to generate core DTO data for testing.
     */
    private final transient DtoMother generate = new DtoMother();

    /**
     * Generate a {@link PartRelationshipsUpdateEvent} containing random data.
     *
     * @return never returns {@literal null}.
     */
    public PartRelationshipsUpdateEvent partRelationshipUpdateList() {
        return PartRelationshipsUpdateEvent.builder()
                .withRelationships(singletonList(partRelationshipUpdate()))
                .build();
    }

    /**
     * Generate a {@link PartRelationshipUpdate} containing random data.
     *
     * @return never returns {@literal null}.
     */
    private PartRelationshipUpdate partRelationshipUpdate() {
        return PartRelationshipUpdate.builder()
                .withRelationship(generate.partRelationship())
                .withRemove(false)
                .withStage(faker.options().option(PartLifecycleStage.class))
                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                .build();
    }

    /**
     * Generate a {@link PartAspectsUpdateEvent} containing random data.
     *
     * @return never returns {@literal null}.
     */
    public PartAspectsUpdateEvent partAspectUpdate() {
        return PartAspectsUpdateEvent.builder()
                .withPart(generate.partId())
                .withAspects(singletonList(generate.partAspect()))
                .withRemove(false)
                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                .build();
    }

    /**
     * Generate a {@link PartAttributeUpdateEvent} containing random data.
     *
     * @return never returns {@literal null}.
     */
    public PartAttributeUpdateEvent partAttributeUpdate() {
        return PartAttributeUpdateEvent.builder()
                .withPart(generate.partId())
                .withName(faker.options().option(PartAttribute.class).name())
                .withValue(faker.commerce().productName())
                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                .build();
    }
}
