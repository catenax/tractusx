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

import io.restassured.RestAssured;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Tag("SystemTests")
public class ConnectorSystemTests {

    @Test
    public void downloadFile() throws Exception {

        var payload = UUID.randomUUID().toString();

        var namespace = "prs-connectors";
        var pod = "prs-connector-provider-0";

        Process exec0 = Runtime.getRuntime().exec(new String[]{
                        "kubectl",
                        "exec",
                        "-n",
                        namespace,
                        pod,
                        "--",
                        "sh",
                        "-c",
                        "echo " + payload + " > /tmp/copy/source/test-document.txt"
                }
        );
        assertThat(exec0.waitFor())
                .as("kubectl command failed")
                .isEqualTo(0);

        var destFile = "/tmp/copy/dest/" + UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("filename", "test-document");
        params.put("connectorAddress", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/prs-connector-provider");
        params.put("destinationPath", destFile);

        RestAssured.baseURI = "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/prs-connector-consumer";
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

        assertThat(requestId).satisfies(s -> UUID.fromString(s));

        await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
                    Process exec = Runtime.getRuntime().exec(new String[]{
                                    "kubectl",
                                    "exec",
                                    "-n",
                                    namespace,
                                    pod,
                                    "--",
                                    "cat",
                                    destFile
                            }
                    );
                    try (InputStream inputStream = exec.getInputStream()) {
                        assertThat(inputStream).hasContent(payload);
                    }
                    exec.waitFor();
                });
    }
}
