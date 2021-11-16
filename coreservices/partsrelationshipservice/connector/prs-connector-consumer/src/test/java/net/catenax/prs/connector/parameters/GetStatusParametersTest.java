package net.catenax.prs.connector.parameters;

import com.github.javafaker.Faker;
import jakarta.validation.Validator;
import net.catenax.prs.connector.testing.ValidatorUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static net.catenax.prs.connector.testing.SetOfConstraintViolationsAssertions.assertThat;


class GetStatusParametersTest {

    static Validator validator = ValidatorUtils.createValidator();

    Faker faker = new Faker();
    String requestId = faker.lorem().characters();

    @Test
    public void validate_OnValidRequestId_ReturnsNoViolations() {
        // Act
        var response = validator.validate(new GetStatusParameters(requestId));
        // Assert
        assertThat(response).hasNoViolations();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n"})
    public void validate_OnBlankRequestId_ReturnsError(String requestId) {
        // Act
        var response = validator.validate(new GetStatusParameters(requestId));
        // Assert
        assertThat(response).hasViolationWithPath("requestId");
    }
}
