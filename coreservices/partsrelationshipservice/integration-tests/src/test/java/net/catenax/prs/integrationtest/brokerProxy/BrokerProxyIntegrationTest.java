//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.integrationtest.brokerProxy;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.catenax.brokerproxy.BrokerProxyApplication;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;
import static net.catenax.prs.testing.TestUtil.DISABLE_FLYWAY;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Tag("BrokerProxyIntegrationTests")
@TestPropertySource(properties = DISABLE_FLYWAY)
@SpringBootTest(classes = {BrokerProxyApplication.class}, webEnvironment = RANDOM_PORT)
public class BrokerProxyIntegrationTest {

    private static final String PATH = "/brokerproxy/v0.1/PartAttributeUpdate";

    private final BrokerProxyMother brokerProxyMother = new BrokerProxyMother();

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void updatedPartsAttributes_success() {

        given()
                .contentType(ContentType.JSON)
                .body(brokerProxyMother.partAttributeUpdate())
        .when()
                .post(PATH)
        .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
