//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.systemtest;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static net.catenax.prs.dtos.PartsTreeView.AS_BUILT;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * System tests verify that PRS returns expected loaded data for the given environment
 *
 * @see <a href="https://confluence.catena-x.net/display/CXM/PRS+Testing+Strategy">PRS Testing Strategy</a>
 */
@Tag("SystemTests")
public class SystemTests extends SystemTestsBase {

    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";

    @Test
    public void getPartsTreeByOneIdAndObjectId(TestInfo testInfo) throws Exception {
        var environment = System.getProperty("environment", "dev");
        // Assumptions.assumeTrue("dev".equals(environment), "Test only available on dev environment");

        var response =
                given()
                        .spec(getRequestSpecification())
                        .baseUri(prsApiUri)
                        .pathParam(ONE_ID_MANUFACTURER, VEHICLE_ONEID)
                        .pathParam(OBJECT_ID_MANUFACTURER, VEHICLE_OBJECTID)
                        .queryParam(VIEW, AS_BUILT)
                        .queryParam(ASPECT, ASPECT_MATERIAL)
                        .queryParam(DEPTH, 2)
                        .when()
                        .get(PATH_BY_IDS)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(getResourceAsString(
                        String.format("%s-%s-expected.json",
                                testInfo.getTestMethod().get().getName(),
                                environment)));
    }

    private String getResourceAsString(String resourceName) throws IOException {
        var resource = getClass().getResourceAsStream(resourceName);
        assertThat(resource).withFailMessage("Missing resource: " + resourceName).isNotNull();
        return new String(resource.readAllBytes());
    }
}
