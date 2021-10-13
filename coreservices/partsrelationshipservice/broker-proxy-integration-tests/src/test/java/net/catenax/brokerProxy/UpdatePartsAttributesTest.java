//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerProxy;

import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import net.catenax.brokerproxy.requests.PartAttributeUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;

public class UpdatePartsAttributesTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partAttributeUpdate";

    @Test
    public void updatedPartsAttributes_success() {

        var updateRequest = brokerProxyMother.partAttributeUpdate();

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(updateRequest, PartAttributeUpdateEvent.class, this::isEqual)).isTrue();
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
    public void updatedPartsAttributesWrongAttributeName_failure() throws JsonProcessingException {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(brokerProxyMother.partAttributeUpdateWrongName())
                .when()
                    .post(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("name:Invalid attribute name.")));
    }

    @Test
    public void updatedPartsAttributesNoEffectTime_failure() throws JsonProcessingException {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(brokerProxyMother.partAttributeUpdateNoEffectTime())
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

    @Test
    public void updatedPartsAttributesNoAttrValue_failure() throws JsonProcessingException {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(brokerProxyMother.partAttributeUpdateNoAttributeValue())
                .when()
                    .post(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("value:must not be null")));
    }

    @Test
    public void updatedPartsAttributesNoPartId_failure() throws JsonProcessingException {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(brokerProxyMother.partAttributeUpdateNoPartId())
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

    private boolean isEqual(PartAttributeUpdateRequest request, PartAttributeUpdateEvent event) {
        return event.getPart().equals(request.getPart())
                && event.getEffectTime().equals(request.getEffectTime())
                && event.getName().equals(request.getName())
                && event.getValue().equals(request.getValue());
    }

}
