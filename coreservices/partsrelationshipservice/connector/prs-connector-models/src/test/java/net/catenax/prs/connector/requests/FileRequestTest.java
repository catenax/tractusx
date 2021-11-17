package net.catenax.prs.connector.requests;

import jakarta.validation.Validator;
import net.catenax.prs.connector.requests.FileRequest.FileRequestBuilder;
import net.catenax.prs.connector.testing.ValidatorUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static net.catenax.prs.connector.requests.RequestMother.blank;
import static net.catenax.prs.connector.testing.SetOfConstraintViolationsAssertions.assertThat;


class FileRequestTest {

    private static final String EMPTY = "";
    static Validator validator = ValidatorUtils.createValidator();

    FileRequest request = RequestMother.generateFileRequest();

    @ParameterizedTest(name = "{0}")
    @MethodSource("mutators")
    void validate(String testName, Function<FileRequestBuilder, FileRequestBuilder> mutator, String expectedViolationPath) {
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
                args("valid", identity(), null),

                args("connectorAddress not null", b -> b.connectorAddress(null), "connectorAddress"),
                args("connectorAddress not blank", b -> b.connectorAddress(blank()), "connectorAddress"),
                args("connectorAddress not empty", b -> b.connectorAddress(EMPTY), "connectorAddress"),

                args("partsTreeRequest not null", b -> b.partsTreeRequest(null), "partsTreeRequest"),
                args("partsTreeRequest valid", b -> b.partsTreeRequest(b.build().getPartsTreeRequest().toBuilder().objectIDManufacturer(null).build()), "partsTreeRequest.objectIDManufacturer"),

                args("destinationPath not null", b -> b.destinationPath(null), "destinationPath"),
                args("destinationPath not blank", b -> b.destinationPath(blank()), "destinationPath"),
                args("destinationPath not empty", b -> b.destinationPath(EMPTY), "destinationPath")
        );
    }

    private static Arguments args(String testName,
                                  UnaryOperator<FileRequestBuilder> mutator,
                                  String expectedViolationPath) {
        return Arguments.of(testName, mutator, expectedViolationPath);
    }
}
