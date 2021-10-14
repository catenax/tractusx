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

import com.catenax.partsrelationshipservice.dtos.events.PartAspectsUpdateEvent;
import net.catenax.prs.entities.PartAspectEntity;
import net.catenax.prs.entities.PartAspectEntityKey;
import net.catenax.prs.entities.PartIdEntityPart;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for {@link PartAspectsUpdateEvent} to {@link PartAspectEntity} entity.
 */
@Component
public class PartAspectUpdateEventToEntityMapper {

    /**
     * Map a {@link PartAspectsUpdateEvent} event to {@link PartAspectEntity} entity.
     *
     * @param event see {@link PartAspectsUpdateEvent}
     * @return List of {@link PartAspectEntity} containing data from update event.
     * @param eventTimestamp Timestamp of the event.
     */
    public List<PartAspectEntity> toAspects(final PartAspectsUpdateEvent event, final Instant eventTimestamp) {
        final List<PartAspectEntity> aspectEntityList = new ArrayList<>();

        event.getAspects().forEach(aspectInEvent -> {
            final var partAspectEntityKey = PartAspectEntityKey.builder()
                    .partId(PartIdEntityPart.builder()
                            .objectIDManufacturer(event.getPart().getObjectIDManufacturer())
                            .oneIDManufacturer(event.getPart().getOneIDManufacturer()).build())
                    .name(aspectInEvent.getName())
                    .build();

            final var partAspectEntity = PartAspectEntity.builder()
                    .key(partAspectEntityKey)
                    .effectTime(event.getEffectTime())
                    .url(aspectInEvent.getUrl())
                    .lastModifiedTime(eventTimestamp)
                    .build();

            aspectEntityList.add(partAspectEntity);
        });

        return aspectEntityList;
    }
}
