package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipsWithInfos;
import com.github.javafaker.Faker;
import net.catenax.prs.entities.EntitiesMother;
import net.catenax.prs.entities.PartAttributeEntity;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.repositories.PartAttributeRepository;
import net.catenax.prs.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.requests.PartsTreeByVinRequest;
import net.catenax.prs.requests.RequestMother;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartsTreeQueryByVinServiceTest {

    @Mock
    PartsTreeQueryService queryService;
    @Mock
    PartAttributeRepository attributeRepository;
    @InjectMocks
    PartsTreeQueryByVinService sut;

    Faker faker = new Faker();
    int maxDepth = faker.number().numberBetween(10, 20);

    EntitiesMother generate = new EntitiesMother();
    PartIdEntityPart car1 = generate.partId();
    PartIdEntityPart car2 = generate.partId();
    PartIdEntityPart gearbox1 = generate.partId();
    PartIdEntityPart gearwheel1 = generate.partId();
    Set<PartIdEntityPart> allPartIds = Set.of(car1, gearbox1, gearwheel1);
    PartRelationshipEntity car1_gearbox1 = generate.partRelationship(car1, gearbox1);
    PartRelationshipEntity gearbox1_gearwheel1 = generate.partRelationship(gearbox1, gearwheel1);
    List<PartRelationshipEntity> relationships = List.of(car1_gearbox1, gearbox1_gearwheel1);
    List<PartAttributeEntity> attributes = List.of(generate.partTypeName(gearwheel1), generate.partTypeName(car1));

    RequestMother generateRequest = new RequestMother();
    PartsTreeByObjectIdRequest requestForCar1 = generateRequest.byObjectId(car1);
    PartsTreeByVinRequest requestForCar1Vin = generateRequest.byVin(car1.getObjectIDManufacturer());

    DtoMother generateDto = new DtoMother();
    PartRelationshipsWithInfos resultDto = generateDto.partRelationshipsWithInfos();

    @Test
    public void getPartsTreeWithNoMatch() {
//        when(attributeRepository
        //               .findAllBy(allPartIds, PrsConfiguration.PART_TYPE_NAME_ATTRIBUTE_NAME))
        //              .thenReturn(emptyList());

        // Act
        var response = sut.getPartsTree(requestForCar1Vin);

        // Assert
        assertThat(response).isEmpty();
        verifyNoInteractions(queryService);
    }

    @Test
    public void getPartsTreeWithOneMatch() {
        testGetPartsTree(car1);
    }

    /**
     * If the search returns multiple cars with matching VIN (and different OneIds), return the parts
     * tree for the first matching car (based on query sort order).
     */
    @Test
    public void getPartsTreeWithTwoMatches() {
        testGetPartsTree(car1, car2);
    }

    private void testGetPartsTree(PartIdEntityPart... partIdsMatchingVinSearch) {
        // Arrange
        when(
                attributeRepository.findAll(any(Example.class), any(Sort.class)))
                .thenReturn(Arrays.stream(partIdsMatchingVinSearch).map(v -> generate.partTypeName(v)).collect(Collectors.toList()));

        when(queryService
                .getPartsTree(requestForCar1))
                .thenReturn(Optional.of(resultDto));

        // Act
        var response = sut.getPartsTree(requestForCar1Vin);

        // Assert
        assertThat(response).containsSame(resultDto);
    }
}