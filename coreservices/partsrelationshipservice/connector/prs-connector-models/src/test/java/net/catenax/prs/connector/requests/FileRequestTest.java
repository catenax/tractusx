package net.catenax.prs.connector.requests;

import jakarta.validation.Validator;
import net.catenax.prs.connector.requests.FileRequest.FileRequestBuilder;
import net.catenax.prs.connector.testing.ValidatorUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

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
                Arguments.of("valid", Function.identity(), null),

                Arguments.of("connectorAddress not null", (F) ((b) -> b.connectorAddress(null)), "connectorAddress"),
                Arguments.of("connectorAddress not blank", (F) ((b) -> b.connectorAddress(blank())), "connectorAddress"),
                Arguments.of("connectorAddress not empty", (F) ((b) -> b.connectorAddress(EMPTY)), "connectorAddress"),

                Arguments.of("partsTreeRequest not null", (F) ((b) -> b.partsTreeRequest(null)), "partsTreeRequest"),
                Arguments.of("partsTreeRequest valid", (F) ((b) -> b.partsTreeRequest(b.build().getPartsTreeRequest().toBuilder().objectIDManufacturer(null).build())), "partsTreeRequest.objectIDManufacturer"),

                Arguments.of("destinationPath not null", (F) ((b) -> b.destinationPath(null)), "destinationPath"),
                Arguments.of("destinationPath not blank", (F) ((b) -> b.destinationPath(blank())), "destinationPath"),
                Arguments.of("destinationPath not empty", (F) ((b) -> b.destinationPath(EMPTY)), "destinationPath")
        );
    }

    private interface F extends Function<FileRequestBuilder, FileRequestBuilder> {
    }
}
