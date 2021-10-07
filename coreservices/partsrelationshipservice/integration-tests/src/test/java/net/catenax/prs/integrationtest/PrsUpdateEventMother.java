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

import com.catenax.partsrelationshipservice.dtos.PartAttribute;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.github.javafaker.Faker;
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
     * JavaFaker instance used to generate random data.
     */
    private final transient Faker faker = new Faker();

    /**
     * Dto mother object that generates test data.
     */
    private final DtoMother generate = new DtoMother();

    /**
     * Generate a {@link PartAttributeUpdateEvent} with sample data.
     * @return see {@link PartAttributeUpdateEvent}.
     */
    public PartAttributeUpdateEvent sampleAttributeUpdateEvent() {
        return PartAttributeUpdateEvent.builder()
                .withEffectTime(Instant.now())
                .withPart(generate.partId())
                .withName(PartAttribute.PART_TYPE_NAME)
                .withValue(faker.lorem().word())
                .build();
    }
}
