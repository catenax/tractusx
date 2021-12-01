package net.catenax.prs.dtos;

import com.github.javafaker.Faker;
import net.catenax.prs.dtos.PartRelationship.PartRelationshipBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static org.assertj.core.api.Assertions.assertThat;

public class PartRelationshipTests {

    final static Faker faker = new Faker();
    final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    PartRelationship sut = partRelationship();

    @Test
    public void validateParentChildHaveUniquePartId() {
        //Arrange
        var partId = partId();
        sut = sut.toBuilder()
                .withChild(partId)
                .withParent(partId)
                .build();
        //Act
        var violations = validator.validate(sut);
        //Assert
        var violationMessages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(violationMessages).containsExactly("Parent and Child part identifier must not be same");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("mutators")
    public void validate(String testName, UnaryOperator<PartRelationshipBuilder> mutator, List<String> expectedViolationPaths) {
        sut = mutator.apply(sut.toBuilder()).build();
        //Act
        var violations = validator.validate(sut);
        //Assert
        if (expectedViolationPaths.isEmpty()) {
            assertThat(violations.isEmpty()).isTrue();
        }else {
            var violationPaths = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toList());
            assertThat(violationPaths).containsExactlyInAnyOrderElementsOf(expectedViolationPaths);
        }
    }

    private static Stream<Arguments> mutators() {
        var partId = partId();

        return Stream.of(
                args("Valid", identity(), List.of()),
                args("Parent not null ", b -> b.withParent(null), List.of("parent")),
                args("Child not null ", b -> b.withChild(null), List.of("child")),
                args("Parent OneIDManufacturer not null", b -> b.withParent(partId().toBuilder().withOneIDManufacturer(null).build()), List.of("parent.oneIDManufacturer")),
                args("Parent ObjectIDManufacturer not null", b -> b.withParent(partId().toBuilder().withObjectIDManufacturer(null).build()), List.of("parent.objectIDManufacturer"))
        );
    }

    private static Arguments args(String testName,
                                  UnaryOperator<PartRelationshipBuilder> mutator,
                                  List<String> expectedViolationPaths) {
        return Arguments.of(testName, mutator, expectedViolationPaths);
    }

    /**
     * Generate {@link PartRelationship} with random values.
     *
     * @return a {@link PartRelationship} with random identifiers.
     */
    private static PartRelationship partRelationship() {
        return PartRelationship.builder()
                .withParent(partId())
                .withChild(partId())
                .build();
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
