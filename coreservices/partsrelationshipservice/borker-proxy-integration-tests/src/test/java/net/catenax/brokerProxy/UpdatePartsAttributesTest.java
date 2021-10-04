//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerProxy;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class UpdatePartsAttributesTest extends BrokerProxyIntegrationTestBase{

    private static final String PATH = "/brokerproxy/v0.1/PartAttributeUpdate";

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
