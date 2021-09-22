package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipsWithInfos;
import com.github.javafaker.Faker;
import net.catenax.prs.configuration.PrsConfiguration;
import net.catenax.prs.entities.EntitiesMother;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.mappers.PartRelationshipEntityListToDtoMapper;
import net.catenax.prs.repositories.PartAspectRepository;
import net.catenax.prs.repositories.PartAttributeRepository;
import net.catenax.prs.repositories.PartRelationshipRepository;
import net.catenax.prs.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.requests.RequestMother;
import net.catenax.prs.testing.DtoMother;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartsTreeQueryServiceTests {

    @Mock
    PartRelationshipRepository relationshipRepository;
    @Mock
    PartAspectRepository aspectRepository;
    @Mock
    PartAttributeRepository attributeRepository;
    @Mock
    PartRelationshipEntityListToDtoMapper mapper;
    @Spy
    PrsConfiguration configuration = new PrsConfiguration();
    @InjectMocks
    PartsTreeQueryService sut;

    Faker faker = new Faker();
    int maxDepth = faker.number().numberBetween(10, 20);

    RequestMother generateRequest = new RequestMother();
    PartsTreeByObjectIdRequest request = generateRequest.byObjectId();

    EntitiesMother generate = new EntitiesMother();
    PartIdEntityPart car1 = generate.partId();
    PartIdEntityPart gearbox1 = generate.partId();
    PartIdEntityPart gearwheel1 = generate.partId();
    Set<PartIdEntityPart> allPartIds = Set.of(car1, gearbox1, gearwheel1);
    PartRelationshipEntity car1_gearbox1 = generate.partRelationship(car1, gearbox1);
    PartRelationshipEntity gearbox1_gearwheel1 = generate.partRelationship(gearbox1, gearwheel1);
    List<PartRelationshipEntity> relationships = List.of(car1_gearbox1, gearbox1_gearwheel1);
    List<PartAttributeEntity> attributes = List.of(generate.partTypeName(gearwheel1), generate.partTypeName(car1));

    DtoMother generateDto = new DtoMother();
    PartRelationshipsWithInfos resultDto = generateDto.partRelationshipsWithInfos();

    @Test
    public void getPartsTreeWithNoMatch() {
        when(relationshipRepository
            .getPartsTree(request.getOneIDManufacturer(), request.getObjectIDManufacturer(), Integer.MAX_VALUE))
            .thenReturn(Lists.emptyList());

        // Act
        var response = sut.getPartsTree(request);

        // Assert
        assertThat(response).isEmpty();
        verifyNoInteractions(aspectRepository);
    }

    @Test
    public void getPartsTree() {
        // Arrange
        setUpCollaborators(maxDepth);

        when(mapper
                .toPartRelationshipsWithInfos(relationships, allPartIds, attributes, emptyList()))
                .thenReturn(resultDto);

        // Act
        var response = sut.getPartsTree(request);

        // Assert
        assertThat(response).containsSame(resultDto);
        verifyNoInteractions(aspectRepository);
    }

    @Test
    public void getPartsTreeWithAspect() {
        // Arrange
        var aspect = faker.lorem().word();
        request = request.toBuilder().aspect(aspect).build();

        setUpCollaborators(maxDepth);

        var aspects = List.of(generate.partAspect(car1), generate.partAspect(gearbox1));
        when(aspectRepository
                .findAllBy(allPartIds, aspect))
                .thenReturn(aspects);

        when(mapper
                .toPartRelationshipsWithInfos(relationships, allPartIds, attributes, aspects))
                .thenReturn(resultDto);

        // Act
        var response = sut.getPartsTree(request);

        // Assert
        assertThat(response).containsSame(resultDto);
    }

    @Test
    public void getPartsTreeWithDepthLessThanOrEqualMax() {
        // Arrange
        var depth = faker.number().numberBetween(1, maxDepth);
        getPartsTreeWithDepth(depth, depth);
    }

    @Test
    public void getPartsTreeWithDepthGreaterThanMax() {
        // Arrange
        var depth = faker.number().numberBetween(maxDepth + 1, maxDepth + 10);
        getPartsTreeWithDepth(depth, maxDepth);
    }

    private void getPartsTreeWithDepth(int requestDepth, int actualDepth) {
        request = request.toBuilder().depth(requestDepth).build();

        setUpCollaborators(actualDepth);

        when(mapper
                .toPartRelationshipsWithInfos(relationships, allPartIds, attributes, emptyList()))
                .thenReturn(resultDto);

        // Act
        var response = sut.getPartsTree(request);

        // Assert
        assertThat(response).containsSame(resultDto);
        verifyNoInteractions(aspectRepository);
    }

    private void setUpCollaborators(int maxDepth) {
        configuration.setPartsTreeMaxDepth(maxDepth);

        when(relationshipRepository
                .getPartsTree(request.getOneIDManufacturer(), request.getObjectIDManufacturer(), maxDepth))
                .thenReturn(relationships);

        when(attributeRepository
                .findAllBy(allPartIds, PrsConfiguration.PART_TYPE_NAME_ATTRIBUTE_NAME))
                .thenReturn(attributes);
    }
}