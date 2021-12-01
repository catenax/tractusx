package net.catenax.prs.validators;

import com.github.javafaker.Faker;
import net.catenax.prs.dtos.PartId;
import net.catenax.prs.dtos.PartRelationship;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PartRelationshipValidatorTest {

    final static Faker faker = new Faker();

    PartRelationshipValidator sut = new PartRelationshipValidator();

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("providePartRelationship")
    public void validate(String testName, PartRelationship partRelationship, boolean expected) {
        boolean actual = sut.isValid(partRelationship, mock(ConstraintValidatorContext.class));
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> providePartRelationship() {
        var partId = partId();

        return Stream.of(
                Arguments.of("Null", null, true),
                Arguments.of("Null Parent", PartRelationship.builder()
                        .withParent(null).withChild(partId()).build(),
                        true),
                Arguments.of("Null Child", PartRelationship.builder()
                                .withParent(partId()).withChild(null).build(),
                        true),
                Arguments.of("Parent and child with same part identifier", PartRelationship.builder()
                                .withParent(partId).withChild(partId).build(),
                        false),
                Arguments.of("Parent and child with different part identifier", PartRelationship.builder()
                                .withParent(partId()).withChild(partId()).build(),
                        true)
        );
    }

    /**
     * Generate {@link PartId} with random values.
     *
     * @return a {@link PartId} with random identifiers.
     */
    private static PartId partId() {
        return PartId.builder()
                .withOneIDManufacturer(faker.company().name())
                .withObjectIDManufacturer(faker.lorem().characters(10, 20))
                .build();
    }


}
