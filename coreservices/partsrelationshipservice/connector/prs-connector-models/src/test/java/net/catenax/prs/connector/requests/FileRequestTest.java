package net.catenax.prs.connector.requests;

import jakarta.validation.Validator;
import net.catenax.prs.connector.requests.FileRequest.FileRequestBuilder;
import net.catenax.prs.connector.testing.ValidatorUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static net.catenax.prs.connector.testing.SetOfConstraintViolationsAssertions.assertThat;


class FileRequestTest {

    private static final String EMPTY = "";
    static Validator validator = ValidatorUtils.createValidator();

    FileRequest sut = RequestMother.generateFileRequest();

    @ParameterizedTest(name = "{0}")
    @MethodSource("mutators")
    void validate(String testName, UnaryOperator<FileRequestBuilder> mutator, String expectedViolationPath) {
        sut = mutator.apply(sut.toBuilder()).build();
        // Act
        var response = validator.validate(sut);
        // Assert
        if (expectedViolationPath != null) {
            assertThat(response).hasViolationWithPath(expectedViolationPath);
        } else {
            assertThat(response).hasNoViolations();
        }
    }

    static Stream<Arguments> mutators() {
        return Stream.of(
                args("valid", identity(), null),

                args("partsTreeRequest not null", b -> b.partsTreeRequest(null), "partsTreeRequest"),
                args("partsTreeRequest valid", b -> b.partsTreeRequest(b.build().getPartsTreeRequest().toBuilder().objectIDManufacturer(null).build()), "partsTreeRequest.objectIDManufacturer")
        );
    }

    private static Arguments args(String testName,
                                  UnaryOperator<FileRequestBuilder> mutator,
                                  String expectedViolationPath) {
        return Arguments.of(testName, mutator, expectedViolationPath);
    }
}
