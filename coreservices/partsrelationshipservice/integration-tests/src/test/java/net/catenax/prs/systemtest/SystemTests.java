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

import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static java.lang.String.format;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * System tests verify that PRS returns expected loaded data for the given environment
 *
 * @see <a href="https://confluence.catena-x.net/display/ARTI/MTPDC+Testing">MTPDC Testing</a>
 */
@Tag("SystemTests")
public class SystemTests extends SystemTestsBase {

    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";

    @Test
    void getPartsTreeByOneIdAndObjectId(TestInfo testInfo) throws Exception {
        // Arrange
        var environment = System.getProperty("environment", "dev");

        // getResourceAsStream returns null if resource not found
        var resource = getClass().getResourceAsStream(format("%s-%s-expected.json",
                testInfo.getTestMethod().orElseThrow().getName(),
                environment));

        // skip test on INT environment
        assumeTrue(resource != null, "Test not available on environment " + environment);

        var client = new PartsRelationshipServiceApi();
        client.getApiClient().setBasePath(prsApiUri);

        // Act
        var response =
                client.getPartsTreeByOneIdAndObjectId(
                                VEHICLE_ONEID,
                                VEHICLE_OBJECTID,
                                "AS_BUILT",
                                ASPECT_MATERIAL,
                                2);

        // Assert
        assertThatJson(response)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(new String(resource.readAllBytes()));
    }
}
