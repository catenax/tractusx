package net.catenax.prs.connector.requests;

import com.github.javafaker.Faker;
import jakarta.validation.Validator;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest.PartsTreeByObjectIdRequestBuilder;
import net.catenax.prs.connector.testing.ValidatorUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static net.catenax.prs.connector.requests.RequestMother.blank;
import static net.catenax.prs.connector.testing.SetOfConstraintViolationsAssertions.assertThat;


class PartsTreeByObjectIdRequestTest {

    private static final String EMPTY = "";
    static Validator validator = ValidatorUtils.createValidator();

    static Faker faker = new Faker();

    PartsTreeByObjectIdRequest request = RequestMother.generateApiRequest();

    @ParameterizedTest(name = "{0}")
    @MethodSource("mutators")
    void validate(String testName, Function<PartsTreeByObjectIdRequestBuilder, PartsTreeByObjectIdRequestBuilder> mutator, String expectedViolationPath) {
        request = mutator.apply(request.toBuilder()).build();
        // Act
        var response = validator.validate(request);
        // Assert
        if (expectedViolationPath != null) {
            assertThat(response).hasViolationWithPath(expectedViolationPath);
        }
        else {
            assertThat(response).hasNoViolations();
        }
    }

    static Stream<Arguments> mutators() {
        return Stream.of(
                Arguments.of("valid", Function.identity(), null),

                Arguments.of("oneIDManufacturer not null", (F) ((b) -> b.oneIDManufacturer(null)), "oneIDManufacturer"),
                Arguments.of("oneIDManufacturer not blank", (F) ((b) -> b.oneIDManufacturer(blank())), "oneIDManufacturer"),
                Arguments.of("oneIDManufacturer not empty", (F) ((b) -> b.oneIDManufacturer(EMPTY)), "oneIDManufacturer"),

                Arguments.of("objectIDManufacturer not null", (F) ((b) -> b.objectIDManufacturer(null)), "objectIDManufacturer"),
                Arguments.of("objectIDManufacturer not blank", (F) ((b) -> b.objectIDManufacturer(blank())), "objectIDManufacturer"),
                Arguments.of("objectIDManufacturer not empty", (F) ((b) -> b.objectIDManufacturer(EMPTY)), "objectIDManufacturer"),

                Arguments.of("view not null", (F) ((b) -> b.view(null)), "view"),
                Arguments.of("view not blank", (F) ((b) -> b.view(blank())), "view"),
                Arguments.of("view not empty", (F) ((b) -> b.view(EMPTY)), "view"),

                Arguments.of("aspect may be null", (F) ((b) -> b.aspect(null)), null),
                Arguments.of("aspect may be blank", (F) ((b) -> b.aspect(blank())), null),
                Arguments.of("aspect may be empty", (F) ((b) -> b.aspect(EMPTY)), null),

                Arguments.of("depth may be null", (F) ((b) -> b.depth(null)), null),
                Arguments.of("depth not 0", (F) ((b) -> b.depth(0)), "depth"),
                Arguments.of("depth not -1", (F) ((b) -> b.depth(-1)), "depth"),
                Arguments.of("depth not negative", (F) ((b) -> b.depth(faker.number().numberBetween(Integer.MIN_VALUE, -1))), "depth")
        );
    }

    private interface F extends Function<PartsTreeByObjectIdRequestBuilder, PartsTreeByObjectIdRequestBuilder> {
    }
}
