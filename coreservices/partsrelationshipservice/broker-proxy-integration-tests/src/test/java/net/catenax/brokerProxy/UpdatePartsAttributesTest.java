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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
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
                .isEqualTo(generateResponse.invalidArgument(List.of("name:Invalid attribute name.")));
    }

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("provideInvalidEffectTime")
    public void updatedPartsAttributesInvalidEffectTime_failure(String name, Instant effectTime, String expectedError) {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(generate.partAttributeUpdate().toBuilder().withEffectTime(effectTime).build())
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
                .isEqualTo(generateResponse.invalidArgument(List.of("part:must not be null")));
    }
}
