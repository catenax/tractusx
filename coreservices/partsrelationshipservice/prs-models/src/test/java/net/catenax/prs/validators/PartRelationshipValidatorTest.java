package net.catenax.prs.validators;

import net.catenax.prs.dtos.PartRelationship;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PartRelationshipValidatorTest {

    PartRelationshipValidator sut = new PartRelationshipValidator();

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("providePartRelationship")
    public void validate(String testName, PartRelationship partRelationship, boolean expected) {
        boolean actual = sut.isValid(partRelationship, mock(ConstraintValidatorContext.class));
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> providePartRelationship() {
        return Stream.of(
                Arguments.of("Null", null, true)
        );
    }
}
