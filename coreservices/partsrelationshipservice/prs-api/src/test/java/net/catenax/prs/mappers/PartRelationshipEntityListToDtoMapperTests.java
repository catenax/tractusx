package net.catenax.prs.mappers;

import com.catenax.partsrelationshipservice.dtos.*;
import net.catenax.prs.entities.*;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartRelationshipEntityListToDtoMapperTests {

    EntitiesMother generate = new EntitiesMother();
    DtoMother generateDto = new DtoMother();
    @Mock
    PartAspectEntityToDtoMapper aspectMapper;
    @Mock
    PartRelationshipEntityToDtoMapper relationshipMapper;
    @Mock
    PartIdEntityPartToDtoMapper idMapper;
    @InjectMocks
    PartRelationshipEntityListToDtoMapper sut;

    PartIdEntityPart car1 = generate.partId();
    PartIdEntityPart gearbox1 = generate.partId();
    PartIdEntityPart gearwheel1 = generate.partId();
    PartRelationshipEntity car1_gearbox1 = generate.partRelationship(car1, gearbox1);
    PartRelationshipEntity gearbox1_gearwheel1 = generate.partRelationship(gearbox1, gearwheel1);
    PartAspectEntity car1_a = generate.partAspect(car1);
    PartAspectEntity gearwheel1_a = generate.partAspect(gearwheel1);
    List<PartRelationshipEntity> source = List.of(car1_gearbox1, gearbox1_gearwheel1);
    List<PartIdEntityPart> allPartIds = List.of(car1, gearbox1, gearwheel1);
    List<PartAspectEntity> allAspects = List.of(car1_a, gearwheel1_a);
    List<PartRelationship> sourceDto = source.stream().map(s -> generateDto.partRelationship()).collect(Collectors.toList());
    List<PartId> sourceIdDto = allPartIds.stream().map(s -> generateDto.partId()).collect(Collectors.toList());
    List<Aspect> aspectsDto = allAspects.stream().map(s -> generateDto.partAspect()).collect(Collectors.toList());
    List<PartAttributeEntity> typeNames = allPartIds.stream().map(p -> generate.partTypeName(p)).collect(Collectors.toList());

    @Test
    void toPartRelationshipsWithInfos() {
        // Arrange
        IntStream.range(0, source.size())
                .forEach(i -> when(idMapper.toPartId(allPartIds.get(i))).thenReturn(sourceIdDto.get(i)));
        IntStream.range(0, source.size())
                .forEach(i -> when(relationshipMapper.toPartRelationship(source.get(i))).thenReturn(sourceDto.get(i)));
        IntStream.range(0, source.size())
                .forEach(i -> when(aspectMapper.toAspect(allAspects.get(i))).thenReturn(aspectsDto.get(i)));

        // Act
        var output = sut.toPartRelationshipsWithInfos(source, allPartIds, typeNames, allAspects);

        // Assert
        List<PartInfo> partInfos = List.of(
                generateDto.partInfo(sourceIdDto.get(0), typeNames.get(0).getValue(), aspectsDto.get(0)),
                generateDto.partInfo(sourceIdDto.get(1), typeNames.get(1).getValue(), null),
                // FIXME partId should not be null
                generateDto.partInfo(null, typeNames.get(2).getValue(), aspectsDto.get(1))
        );
        assertThat(output).usingRecursiveComparison()
                .isEqualTo(
                        (PartRelationshipsWithInfos.builder()
                                .withRelationships(sourceDto)
                                .withPartInfos(partInfos)
                                .build()));
    }
}
