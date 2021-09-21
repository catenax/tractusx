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
import io.restassured.RestAssured;
import net.catenax.prs.PrsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_MAINTAINED;
import static io.restassured.RestAssured.given;
import static net.catenax.prs.testing.TestUtil.DATABASE_TESTCONTAINER;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(classes = {PrsApplication.class}, webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = DATABASE_TESTCONTAINER)
public class PrsIntegrationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUpClass(){
        RestAssured.port = port;
    }

    @Test
    public void getPartsTreeByVin() throws Exception {
        // Arrange
        var objectMapper = new ObjectMapper();
        var expected = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("response_1631610272167.json"), PartRelationshipsWithInfos.class);

        // Act
        var response =
            given()
                .pathParam("vin", "BMWOVCDI21L5DYEUU")
                .queryParam("view", AS_MAINTAINED)
            .when()
                .get("/api/v0.1/vins/{vin}/partsTree")
            .then()
                .assertThat()
                    .statusCode(HttpStatus.OK.value())
            .extract().asString();

        // Assert
        assertThatJson(response).isEqualTo(json(expected));
    }
}