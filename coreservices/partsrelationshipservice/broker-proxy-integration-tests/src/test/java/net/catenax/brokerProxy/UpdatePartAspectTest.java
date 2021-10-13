package net.catenax.brokerProxy;

import com.catenax.partsrelationshipservice.dtos.events.PartAspectUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartAspectTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partAspectUpdate";

    @Test
    public void updatedPartAspectUpdate_success() {

        var updateRequest = brokerProxyMother.partAspectUpdate();

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(updateRequest, PartAspectUpdateRequest.class, this::isEqual)).isTrue();

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

    @Test
    public void updatedPartAspectUpdateEmptyAspectList_failure() throws JsonProcessingException {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(brokerProxyMother.partAspectUpdateEmptyList())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("aspects:Aspects list can't be empty. Use remove field to remove part aspects.")));

    }

    @Test
    public void updatedPartAspectUpdateWithNoPartId_failure() throws JsonProcessingException {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(brokerProxyMother.partAspectUpdateNoPartId())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("part:must not be null")));

    }

    @Test
    public void updatedPartAspectUpdateWithNoEffectTime_failure() throws JsonProcessingException {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(brokerProxyMother.partAspectUpdateNoEffectTime())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("effectTime:must not be null")));

    }

    private boolean isEqual(PartAspectUpdateRequest request, PartAspectUpdateRequest event) {
        return event.getPart().equals(request.getPart())
                && event.getEffectTime().equals(request.getEffectTime())
                && event.getAspects().equals(request.getAspects())
                && event.isRemove() == request.isRemove();
    }
}
