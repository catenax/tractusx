package net.catenax.prs.mappers;

import net.catenax.prs.testing.PartUpdateEventMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartAttributeUpdateEventToEntityMapperTests {
    PartUpdateEventMother generate = new PartUpdateEventMother();
    PartAttributeUpdateEventToEntityMapper sut = new PartAttributeUpdateEventToEntityMapper();

    @Test
    void toAttribute() {
        var input = generate.attributeUpdateEvent();
        var output = sut.toAttribute(input);

        assertThat(output.getEffectTime()).isEqualTo(input.getEffectTime());
        assertThat(output.getValue()).isEqualTo(input.getValue());
        assertThat(output.getLastModifiedTime()).isNotNull();
        assertThat(output.getKey().getAttribute()).isEqualTo(input.getName().name());
        assertThat(output.getKey().getPartId().getObjectIDManufacturer()).isEqualTo(input.getPart().getObjectIDManufacturer());
        assertThat(output.getKey().getPartId().getOneIDManufacturer()).isEqualTo(input.getPart().getOneIDManufacturer());
    }
}
