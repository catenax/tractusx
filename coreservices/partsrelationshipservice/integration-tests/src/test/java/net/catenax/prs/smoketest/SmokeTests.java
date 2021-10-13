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

import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import net.catenax.prs.e2etest.E2ETestBase;
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
public class SmokeTests extends E2ETestBase {

    @Test
    public void getPartsTreeByVin_success() {

        var specificationBuilder = new RequestSpecBuilder();

        // Add basic auth if a userName and password have been specified.
        if (userName != null && password != null) {
            var auth = new BasicAuthScheme();
            auth.setUserName(userName);
            auth.setPassword(password);
            specificationBuilder.setAuth(auth).build();
        }

        var specification = specificationBuilder.build();

        given()
            .spec(specification)
            .pathParam(VIN, SAMPLE_VIN)
            .queryParam(VIEW, AS_MAINTAINED)
        .when()
            .get(PATH_BY_VIN)
        .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("relationships", hasSize(greaterThan(0)))
            .body("partInfos", hasSize(greaterThan(0)));
    }

}
