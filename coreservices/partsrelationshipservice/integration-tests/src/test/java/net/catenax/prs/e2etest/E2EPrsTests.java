//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.e2etest;

import com.catenax.partsrelationshipservice.dtos.*;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.catenax.brokerproxy.requests.PartRelationshipUpdate;
import net.catenax.brokerproxy.requests.PartRelationshipUpdateRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_MAINTAINED;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Smoke tests verify that the cloud infrastructure where PRS runs is working as expected
 * @see <a href="https://confluence.catena-x.net/display/CXM/PRS+Testing+Strategy">PRS Testing Strategy</a>
 */
public class E2EPrsTests extends E2ETestBase {

    private Faker faker = new Faker();

    @Test
    @Disabled
    public void updateRelationshipsAndGetPartsTree_success() {

        RequestSpecification specification = getRequestSpecification();

        PartId child = PartId.builder()
                .withOneIDManufacturer(faker.company().name())
                .withObjectIDManufacturer(faker.lorem().characters(10, 20))
                .build();
        PartId parent = PartId.builder()
                .withOneIDManufacturer(faker.company().name())
                .withObjectIDManufacturer(faker.lorem().characters(10, 20))
                .build();
        PartRelationship partRelationship = PartRelationship.builder()
                .withParent(parent)
                .withChild(child)
                .build();
        var updateRequest = PartRelationshipUpdateRequest.builder()
                .withRelationships(List.of(
                        PartRelationshipUpdate.builder()
                                .withEffectTime(Instant.now())
                                .withRemove(false)
                                .withStage(PartLifecycleStage.BUILD)
                                .withRelationship(partRelationship)
                                .build()))
                .build();

        given()
            .contentType(ContentType.JSON)
            .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
            .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
            .queryParam(VIEW, PartsTreeView.AS_MAINTAINED)
            .body(updateRequest)
        .when()
            .post(PATH_UPDATE_ATTRIBUTES)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        var response =
            given()
                .spec(specification)
                .pathParam(VIN, SAMPLE_VIN)
                .queryParam(VIEW, AS_MAINTAINED)
            .when()
                .get(PATH_BY_IDS)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).containsOnly(partRelationship);
    }

}
