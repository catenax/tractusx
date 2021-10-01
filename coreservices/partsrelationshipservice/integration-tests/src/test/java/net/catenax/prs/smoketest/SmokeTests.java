//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.smoketest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_MAINTAINED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

/**
 * Smoke tests verify that the cloud infrastructure where PRS runs is working as expected
 * @see <a href="https://confluence.catena-x.net/display/CXM/PRS+Testing+Strategy">PRS Testing Strategy</a>
 */
@Tag("SmokeTests")
public class SmokeTests {

    private static final String PATH = "/api/v0.1/vins/{vin}/partsTree";
    private static final String SAMPLE_VIN = "YS3DD78N4X7055320";
    private static final String VIN = "vin";
    private static final String VIEW = "view";
    private String userName;
    private String password;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = System.getProperty("baseURI");
        userName = System.getProperty("userName");
        password = System.getProperty("password");
    }

    @Test
    public void getPartsTreeByVin_success() {
        // Add basic auth if a userName and password have been specified.
        var requestSpecification = (userName != null && password != null) ?
                given().auth().basic(userName, password) : given();

        requestSpecification
            .pathParam(VIN, SAMPLE_VIN)
            .queryParam(VIEW, AS_MAINTAINED)
        .when()
            .get(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("relationships", hasSize(greaterThan(0)))
            .body("partInfos", hasSize(greaterThan(0)));
    }

}
