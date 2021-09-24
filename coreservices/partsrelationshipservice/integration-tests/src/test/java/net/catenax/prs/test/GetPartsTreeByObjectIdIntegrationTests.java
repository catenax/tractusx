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
import net.catenax.prs.configuration.PrsConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    private static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    private static final String VIEW = "view";
    private static final String DEPTH = "depth";

    /**
     * PRS configuration settings.
     */
    @Autowired
    private PrsConfiguration configuration;

    @Test
    public void getPartsTreeByObjectId_maintainedView_success() throws Exception {
        var objectMapper = new ObjectMapper();
        var expected = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("sample_part_response.json"), PartRelationshipsWithInfos.class);

        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, PART_ONE_ID)
                .pathParam(OBJECT_ID_MANUFACTURER, PART_OBJECT_ID)
                .queryParam(VIEW, AS_MAINTAINED)
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
            .pathParam(ONE_ID_MANUFACTURER, PART_ONE_ID)
            .pathParam(OBJECT_ID_MANUFACTURER, "not-existing-object-id")
            .queryParam(VIEW, AS_MAINTAINED)
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
            .pathParam(ONE_ID_MANUFACTURER, "not-existing-one-id")
            .pathParam(OBJECT_ID_MANUFACTURER, PART_OBJECT_ID)
            .queryParam(VIEW, AS_MAINTAINED)
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
            .pathParam(ONE_ID_MANUFACTURER, PART_ONE_ID)
            .pathParam(OBJECT_ID_MANUFACTURER, PART_OBJECT_ID)
        .when()
            .get(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPartsTreeByObjectId_exceedMaxDepth_returns400() {
        var maxDepth = configuration.getPartsTreeMaxDepth();
        given()
                .pathParam(ONE_ID_MANUFACTURER, PART_ONE_ID)
                .pathParam(OBJECT_ID_MANUFACTURER, PART_OBJECT_ID)
                .queryParam(VIEW, AS_MAINTAINED)
                .queryParam(DEPTH, maxDepth + 1)
        .when()
                .get(PATH)
        .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}