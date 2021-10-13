package net.catenax.brokerProxy;

import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartRelationshipTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partRelationshipUpdateList";

    @Test
    public void updatedPartsRelationships_success() throws Exception {

        var updateRequest = brokerProxyMother.partRelationshipUpdate();

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(updateRequest, PartRelationshipUpdateRequest.class)).isTrue();
    }

    @Test
    public void updatedPartsAttributesBadRequest_failure() {

        given()
            .contentType(ContentType.JSON)
            .body("bad request")
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatedPartsAttributesWithNoRelationships_failure() throws JsonProcessingException {

        var updateRequest = brokerProxyMother.partRelationshipUpdateNoRelationships();

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("relationships:must not be empty")));
    }

    @Test
    public void updatedPartsAttributesWithNoEffectTime_failure() throws JsonProcessingException {

        var updateRequest = brokerProxyMother.partRelationshipUpdateNoEffectTime();

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("relationships[0].effectTime:must not be null")));
    }

    @Test
    public void updatedPartsAttributesWithNoStage_failure() throws JsonProcessingException {

        var updateRequest = brokerProxyMother.partRelationshipUpdateNoStage();

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("relationships[0].stage:must not be null")));
    }
}
