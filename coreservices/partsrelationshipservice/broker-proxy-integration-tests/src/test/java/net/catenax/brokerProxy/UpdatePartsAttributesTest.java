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

import io.restassured.http.ContentType;
import net.catenax.prs.dtos.events.PartAttributeUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartsAttributesTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partAttributeUpdate";

    @Test
    public void updatedPartsAttributes_success() throws Exception {

        var event = generate.partAttributeUpdate();

        given()
            .contentType(ContentType.JSON)
            .body(event)
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(event, PartAttributeUpdateRequest.class)).isTrue();
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
    public void updatedPartsAttributesWrongAttributeName_failure() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(generate.partAttributeUpdate().toBuilder().withName(faker.lorem().word()).build())
                .when()
                    .post(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(generateResponse.invalidArgument(List.of("name:Invalid attribute name.")));
    }

    @Test
    public void updatedPartsAttributesNoEffectTime_failure() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(generate.partAttributeUpdate().toBuilder().withEffectTime(null).build())
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

    @Test
    public void updatedPartsAttributesNoAttrValue_failure() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(generate.partAttributeUpdate().toBuilder().withValue(null).build())
                .when()
                    .post(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(generateResponse.invalidArgument(List.of("value:must not be null")));
    }

    @Test
    public void updatedPartsAttributesNoPartId_failure() {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(generate.partAttributeUpdate().toBuilder().withPart(null).build())
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
}
