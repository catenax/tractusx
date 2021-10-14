package net.catenax.prs.mappers;

import net.catenax.prs.entities.PartAspectEntity;
import net.catenax.prs.entities.PartAspectEntityKey;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.testing.PartUpdateEventMother;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PartAspectUpdateEventToEntityMapperTests {
    PartUpdateEventMother generate = new PartUpdateEventMother();
    PartAspectUpdateEventToEntityMapper sut = new PartAspectUpdateEventToEntityMapper();


    @Test
    void toAspects() {
        //arrange
        var input = generate.aspectUpdateEvent();
        var eventTimestamp = Instant.now();
        var expectedEntities = input.getAspects().stream()
                .map(updateEvent -> PartAspectEntity.builder()
                        .key(PartAspectEntityKey.builder()
                                .partId(PartIdEntityPart.builder()
                                        .objectIDManufacturer(input.getPart().getObjectIDManufacturer())
                                        .oneIDManufacturer(input.getPart().getOneIDManufacturer())
                                        .build())
                                .name(updateEvent.getName())
                                .build())
                        .url(updateEvent.getUrl())
                        .effectTime(input.getEffectTime())
                        .lastModifiedTime(eventTimestamp)
                        .build()).collect(Collectors.toList());

        //act
        var output = sut.toAspects(input, eventTimestamp);

        //assert
        assertThat(output).usingFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedEntities);
    }
}
