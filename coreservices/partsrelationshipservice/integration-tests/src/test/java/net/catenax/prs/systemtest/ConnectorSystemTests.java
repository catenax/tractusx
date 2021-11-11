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

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Tag("SystemTests")
public class ConnectorSystemTests {

    protected static final Faker faker = new Faker();

    @Test
    public void downloadFile() throws Exception {

        var sharedDirectory = Paths.get("../connector/shared-mount");
        var src = sharedDirectory.resolve("source");
        var dest = sharedDirectory.resolve("dest");
        var payload = faker.lorem().sentence();
        Files.writeString(src.resolve("test-document.txt"), payload);
        var file = dest.resolve(UUID.randomUUID().toString());

        Map<String, String> params = new HashMap<>();
        params.put("filename", "test-document");
        params.put("connectorAddress", "http://provider:8181/");
        params.put("destinationPath", "/tmp/copy/dest/" + file.getFileName().toString());

        RestAssured.baseURI = "http://localhost:9191";
        var requestId =
                given()
                        .contentType("application/json")
                        .body(params)
                .when()
                        .post("/api/file")
                .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract().asString();

        await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
                    var state =
                            given()
                                    .pathParam("requestId", requestId)
                                    .when()
                                    .get("/api/datarequest/{requestId}/state")
                                    .then()
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract().asString();
                    assertThat(state).isEqualTo("COMPLETED");
                });

        assertThat(file).hasContent(payload);
    }
}
