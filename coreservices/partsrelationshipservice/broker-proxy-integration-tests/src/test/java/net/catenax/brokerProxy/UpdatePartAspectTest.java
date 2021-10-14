package net.catenax.brokerProxy;

import com.catenax.partsrelationshipservice.dtos.Aspect;
import com.catenax.partsrelationshipservice.dtos.events.PartAspectsUpdateEvent;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
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

        assertThat(hasExpectedBrokerEvent(event, PartAspectsUpdateEvent.class)).isTrue();

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
    @NullSource
    @EmptySource
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
                .when(IGNORING_ARRAY_ORDER)
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
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(generateResponse.invalidArgument(List.of("part:must not be null")));

    }

    @Test
    public void updatedPartAspectUpdateWithNoEffectTime_failure() {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(generate.partAspectUpdate().toBuilder().withEffectTime(null).build())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(generateResponse.invalidArgument(List.of("effectTime:must not be null")));
    }
}
