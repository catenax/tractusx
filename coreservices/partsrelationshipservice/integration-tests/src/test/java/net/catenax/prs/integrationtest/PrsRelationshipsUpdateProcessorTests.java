//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.integrationtest;

import com.catenax.partsrelationshipservice.dtos.*;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_BUILT;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PrsRelationshipsUpdateProcessorTests extends PrsIntegrationTestsBase {

    private static final String PATH = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    private static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    private static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    private static final String VIEW = "view";

    private final PrsUpdateEventMother sampleEvents = new PrsUpdateEventMother();

    @Test
    @Disabled
    public void updatePartsRelationship_success() {

        //Arrange
        PartRelationshipUpdateEvent.RelationshipUpdate relationshipUpdate = sampleEvents.sampleRelationshipUpdate();
        var event = PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(relationshipUpdate))
                .build();
        PartRelationship relationship = relationshipUpdate.getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent(event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                .queryParam(VIEW, AS_BUILT)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).containsOnly(relationship);
    }

    @Test
    @Disabled
    public void updateTwoPartsRelationships_success() {

        //Arrange
        PartRelationshipUpdateEvent.RelationshipUpdate update1 = sampleEvents.sampleRelationshipUpdate();
        PartRelationshipUpdateEvent.RelationshipUpdate update2 = sampleEvents.sampleRelationshipUpdate();
        var event = PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(update1, update2))
                .build();
        PartRelationship relationship1 = update1.getRelationship();
        PartRelationship relationship2 = update2.getRelationship();
        PartId parent1 = relationship1.getParent();

        //Act
        publishUpdateEvent(event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, parent1.getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, parent1.getObjectIDManufacturer())
                .queryParam(VIEW, AS_BUILT)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).hasSize(2);
        assertThat(response.getRelationships())
                .contains(relationship1, relationship2);
    }

    @Test
    @Disabled
    public void sendWrongMassage_success() {

        //Arrange
        PartRelationshipUpdateEvent.RelationshipUpdate relationshipUpdate = sampleEvents.sampleRelationshipUpdate();
        var event = PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(relationshipUpdate))
                .build();
        PartRelationship relationship = relationshipUpdate.getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent("wrong_message");
        publishUpdateEvent(event);

        //Assert
        var response =
                given()
                    .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                    .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                    .queryParam(VIEW, AS_BUILT)
                .when()
                    .get(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).containsOnly(relationship);
    }

    @Test
    @Disabled
    public void updatePartsRelationshipsWithEffectTimeInTheFuture_NoUpdate_success() {
        //Arrange
        var relationshipUpdate = sampleEvents.sampleRelationshipUpdate(Instant.now().plus(10, ChronoUnit.DAYS));
        var event = PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(relationshipUpdate))
                .build();
        var parent = relationshipUpdate.getRelationship().getParent();

        //Act
        publishUpdateEvent(event);

        //Assert
        var response =
                given()
                    .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                    .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                    .queryParam(VIEW, AS_BUILT)
                .when()
                    .get(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).isEmpty();
    }

    @Test
    @Disabled
    public void updatePartsRelationshipsDuplicateEvent_success() {

        //Arrange
        PartRelationshipUpdateEvent.RelationshipUpdate relationshipUpdate = sampleEvents.sampleRelationshipUpdate();
        var event = PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(relationshipUpdate))
                .build();
        PartRelationship relationship = relationshipUpdate.getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent(event);
        publishUpdateEvent(event);

        //Assert
        var response =
                given()
                    .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                    .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                    .queryParam(VIEW, AS_BUILT)
                .when()
                    .get(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().as(PartRelationshipsWithInfos.class);

        assertThat(response.getRelationships()).containsOnly(relationship);
    }
}
