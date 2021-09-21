package net.catenax.prs;

import com.github.javafaker.Faker;
import net.catenax.prs.entities.PartIdEntityPart;
import net.catenax.prs.entities.PartRelationshipEntity;
import net.catenax.prs.entities.PartRelationshipEntityKey;
import net.catenax.prs.repositories.PartRelationshipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static net.catenax.prs.testing.TestUtil.POSTGRESQL_TESTCONTAINER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@TestPropertySource(properties = {
    POSTGRESQL_TESTCONTAINER,
    "spring.jpa.hibernate.ddl-auto=validate",
})
public class JpaTests {
    @Autowired
    private PartRelationshipRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private final Faker faker = new Faker();

    PartIdEntityPart car1 = generatePartId();
    PartIdEntityPart gearbox1 = generatePartId();
    PartRelationshipEntity car1_gearbox1 = generatePartRelationshipEntity(car1, gearbox1);

    @Test
    void getPartsTreeReturnsRelatedEntities() {
        persistCars();

        assertThat(getPartsTree(car1))
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(car1_gearbox1);
    }

    private void persistCars() {
        entityManager.persist(car1_gearbox1);
    }

    private List<PartRelationshipEntity> getPartsTree(PartIdEntityPart partId) {
        return repository.getPartsTree(
            partId.getOneIDManufacturer(),
            partId.getObjectIDManufacturer(),
            faker.number().numberBetween(10, 1000));
    }

    private PartRelationshipEntity generatePartRelationshipEntity(PartIdEntityPart parentId, PartIdEntityPart childId) {
        return PartRelationshipEntity.builder()
            .key(generatePartRelationshipEntityKey(parentId, childId))
            .uploadDateTime(now())
            .build();
    }

    private PartRelationshipEntityKey generatePartRelationshipEntityKey(PartIdEntityPart parentId,
                                                                        PartIdEntityPart childId) {
        return PartRelationshipEntityKey.builder()
            .childId(childId)
            .parentId(parentId)
            .partRelationshipListId(randomUUID())
            .build();
    }

    private PartIdEntityPart generatePartId() {
        return PartIdEntityPart.builder()
            .oneIDManufacturer(faker.company().name())
            .objectIDManufacturer(faker.lorem().characters(10, 20))
            .build();
    }
}
