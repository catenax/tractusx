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

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@Tag("SystemTests")
public class ConnectorSystemTests {

    private static final String baseURI = System.getProperty("baseURI", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com");
    private static final String namespace = System.getProperty("k8sNamespace", "prs-connectors");
    private static final String pod = System.getProperty("k8sPod", "prs-connector-provider-0");

    @Test
    public void downloadFile() throws Exception {

        var payload = UUID.randomUUID().toString();

        var exec0 = runCommand(
                        "sh",
                        "-c",
                        "echo " + payload + " > /tmp/copy/source/test-document.txt"
                );
        assertThat(exec0.waitFor())
                .as("kubectl command failed")
                .isEqualTo(0);

        var destFile = "/tmp/copy/dest/" + UUID.randomUUID();

        Map<String, String> params = new HashMap<>();
        params.put("filename", "test-document");
        params.put("connectorAddress", baseURI + "/prs-connector-provider");
        params.put("destinationPath", destFile);

        RestAssured.baseURI = baseURI + "/prs-connector-consumer";
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
                    var exec = runCommand("cat", destFile);
                    try (InputStream inputStream = exec.getInputStream()) {
                        assertThat(inputStream).hasContent(payload);
                    }
                    exec.waitFor();
                });
    }

    private Process runCommand(String... params) throws IOException {
        var l = new ArrayList<>(Arrays.asList(
                "kubectl",
                "exec",
                "-n",
                namespace,
                pod,
                "--"));
        l.addAll(Arrays.asList(params));
        return new ProcessBuilder()
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .command(l)
                .start();
    }
}
