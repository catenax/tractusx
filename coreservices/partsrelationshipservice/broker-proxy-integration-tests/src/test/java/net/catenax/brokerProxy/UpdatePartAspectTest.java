package net.catenax.brokerProxy;

import io.restassured.http.ContentType;
import net.catenax.prs.dtos.Aspect;
import net.catenax.prs.dtos.events.PartAspectsUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartAspectTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partAspectUpdate";

    @Test
    public void updatedPartAspectUpdate_success() throws Exception {

        var event = generate.partAspectUpdate();

        given()
            .contentType(ContentType.JSON)
            .body(event)
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(event, PartAspectsUpdateRequest.class)).isTrue();

    }

    @Test
    public void updatedPartAspectUpdateBadRequest_failure() {

        given()
            .contentType(ContentType.JSON)
            .body("bad request")
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void updatedPartAspectUpdateWithNoAspects_failure(List<Aspect> aspects) {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(generate.partAspectUpdate().toBuilder().withAspects(aspects).build())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of("aspects:Aspects list can't be empty. Use remove field to remove part aspects.")));

    }

    @Test
    public void updatedPartAspectUpdateWithNoPartId_failure() {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(generate.partAspectUpdate().toBuilder().withPart(null).build())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of("part:must not be null")));

    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideInvalidEffectTime")
    public void updatedPartAspectUpdateWithInvalidEffectTime_failure(Instant effectTime, String expectedError) {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(generate.partAspectUpdate().toBuilder().withEffectTime(effectTime).build())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of(expectedError)));
    }
}
