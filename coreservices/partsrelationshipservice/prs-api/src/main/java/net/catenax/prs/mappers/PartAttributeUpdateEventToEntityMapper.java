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

import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartAttributeEntityKey;
import net.catenax.prs.entities.PartIdEntityPart;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Mapper for {@link PartAttributeUpdateEvent} to {@link PartAttributeEntity} entity.
 */
@Component
public class PartAttributeUpdateEventToEntityMapper {

    /**
     * Map a {@link PartAttributeUpdateEvent} event to {@link PartAttributeEntity} entity.
     *
     * @param event see {@link PartAttributeUpdateEvent}
     * @return {@link PartAttributeEntity} containing data from update event.
     */
    public PartAttributeEntity toAttribute(final PartAttributeUpdateEvent event) {
        final var partAttributeEntityKey = PartAttributeEntityKey.builder()
                .attribute(event.getName().name())
                .partId(PartIdEntityPart.builder()
                        .objectIDManufacturer(event.getPart().getObjectIDManufacturer())
                        .oneIDManufacturer(event.getPart().getOneIDManufacturer()).build())
                .build();

        return PartAttributeEntity.builder()
                .key(partAttributeEntityKey)
                .value(event.getValue())
                .effectTime(event.getEffectTime())
                .lastModifiedTime(Instant.now())
                .build();
    }
}
