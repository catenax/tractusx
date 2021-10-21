package net.catenax.brokerProxy;

import io.restassured.http.ContentType;
import net.catenax.prs.dtos.Aspect;
import net.catenax.prs.dtos.events.PartAspectsUpdateRequest;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartAspectTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partAspectUpdate";
    private static final DtoMother generateDto = new DtoMother();

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

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideInvalidAspects")
    public void updatedPartAspectUpdateWithInvalidAspects_failure(List<Aspect> aspects, String expectedError) {

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
                .isEqualTo(generateResponse.invalidArgument(List.of(expectedError)));

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

    /**
     * Provides invalid aspects test data.
     * @return Invalid aspects as {@link Stream} of {@link Arguments}.
     */
    private static Stream<Arguments> provideInvalidAspects() {
        return Stream.of(
                Arguments.of(null, "aspects:Aspects list can't be empty. Use remove field to remove part aspects."),
                Arguments.of(Collections.emptyList(), "aspects:Aspects list can't be empty. Use remove field to remove part aspects."),
                Arguments.of(List.of(generateDto.partAspect().toBuilder().withName(null).build()), "aspects[0].name:must not be blank"),
                Arguments.of(List.of(generateDto.partAspect().toBuilder().withUrl(null).build()), "aspects[0].url:must not be blank")
        );
    }
}
