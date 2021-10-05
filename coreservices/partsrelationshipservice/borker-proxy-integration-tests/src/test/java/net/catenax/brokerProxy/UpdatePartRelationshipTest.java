package net.catenax.brokerProxy;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class UpdatePartRelationshipTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/brokerproxy/v0.1/PartRelationshipUpdateList";

    @Test
    public void updatedPartsAttributes_success() {

        given()
                .contentType(ContentType.JSON)
                .body(brokerProxyMother.partRelationshipUpdate())
                .when()
                .post(PATH)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
