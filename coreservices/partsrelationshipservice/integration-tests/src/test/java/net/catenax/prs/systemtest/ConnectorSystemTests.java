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

import net.catenax.prs.requests.PartsTreeByObjectIdRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static net.catenax.prs.systemtest.SystemTestsBase.ASPECT_MATERIAL;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


/**
 * System tests that verify the interaction between Consumer and Provider connectors.
 * <p>
 * The current implementation expects the Provider to be a singleton pod.
 *
 * @see <a href="https://confluence.catena-x.net/display/ARTI/MTPDC+Testing">MTPDC Testing</a>
 */
@Tag("SystemTests")
public class ConnectorSystemTests {

    private static final String consumerURI = System.getProperty("ConnectorConsumerURI",
            "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/prs-connector-consumer");
    private static final String providerURI = System.getProperty("ConnectorProviderURI",
            "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/bmw/mtpdc/connector");
    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";

    @Test
    public void downloadFile() throws Exception {

        // Arrange
        var environment = System.getProperty("environment", "dev");

        // Temporarily hardcode the file path. It will change when adding several providers.
        var fileWithExpectedOutput = format("getPartsTreeByOneIdAndObjectId-%s-bmw-expected.json", environment);
        var expectedResult = new String(getClass().getResourceAsStream(fileWithExpectedOutput).readAllBytes());

        // Act

        // Send query to Consumer connector, to perform file copy on Provider
        var destFile = UUID.randomUUID();
        Map<String, Object> params = new HashMap<>();
        params.put("filename", "test-document");
        params.put("connectorAddress", providerURI);
        params.put("destinationPath", destFile);
        params.put("partsTreeRequest", PartsTreeByObjectIdRequest.builder()
                .oneIDManufacturer(VEHICLE_ONEID)
                .objectIDManufacturer(VEHICLE_OBJECTID)
                .view("AS_BUILT")
                .aspect(ASPECT_MATERIAL)
                .depth(2)
                .build());

        var requestId =
                given()
                        .baseUri(consumerURI)
                        .contentType("application/json")
                        .body(params)
                .when()
                        .post("/api/v0.1/file")
                .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract().asString();

        // An ID is returned, for polling
        assertThat(requestId).isNotBlank();

        // Expect the destination file to appear on the Provider pod
        await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> getSasUrl(requestId));

        // retrieve blob
        var sasUrl = getSasUrl(requestId);

        // Assert
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(sasUrl))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        var result = response.body();

        // We suspect the connectorSystemTests to be flaky when running right after the deployment workflow.
        // But it is hard to reproduce, so logging the results, to help when this will happen again.
        System.out.println(String.format("expectedResult: %s", expectedResult));
        System.out.println(String.format("Result: %s", result));
        assertThatJson(result)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResult);
    }

    private String getSasUrl(String requestId) {
        return
                given()
                        .baseUri(consumerURI)
                        .pathParam("requestId", requestId)
                .when()
                        .get("/api/v0.1/datarequest/{requestId}/state")
                .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract().asString();
    }
}
