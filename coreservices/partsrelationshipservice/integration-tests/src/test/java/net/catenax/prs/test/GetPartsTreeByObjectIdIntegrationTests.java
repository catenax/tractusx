//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.test;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipsWithInfos;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.assertj.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_MAINTAINED;
import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;

public class GetPartsTreeByObjectIdIntegrationTests extends PrsIntegrationTestsBase {

    private static final String PATH = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    private static final String PART_ONE_ID = "ZF";
    private static final String PART_OBJECT_ID = "I88HJHS45";

    @Test
    public void getPartsTreeByObjectId_maintainedView_success() throws Exception {
        var objectMapper = new ObjectMapper();
        var expected = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("sample_part_response.json"), PartRelationshipsWithInfos.class);

        var response =
            given()
                .pathParam("oneIDManufacturer", PART_ONE_ID)
                .pathParam("objectIDManufacturer", PART_OBJECT_ID)
                .queryParam("view", AS_MAINTAINED)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                    .statusCode(HttpStatus.OK.value())
            .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(json(expected));
    }

    @Test
    public void getPartsTreeByObjectId_notExistingObjectid_returns404() throws Exception {
        var expected = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("empty_response.json").toURI())));

        var response =
        given()
            .pathParam("oneIDManufacturer", PART_ONE_ID)
            .pathParam("objectIDManufacturer", "not-existing-object-id")
            .queryParam("view", AS_MAINTAINED)
        .when()
            .get(PATH)
        .then()
            .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(json(expected));
    }

    @Test
    public void getPartsTreeByObjectId_notExistingOneId_returns404() throws Exception {
        var expected = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("empty_response.json").toURI())));

        var response =
        given()
            .pathParam("oneIDManufacturer", "not-existing-one-id")
            .pathParam("objectIDManufacturer", PART_OBJECT_ID)
            .queryParam("view", AS_MAINTAINED)
        .when()
            .get(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(json(expected));
    }

    @Test
    public void getPartsTreeByObjectId_noView_returns400() {
        given()
            .pathParam("oneIDManufacturer", PART_ONE_ID)
            .pathParam("objectIDManufacturer", PART_OBJECT_ID)
        .when()
            .get(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}