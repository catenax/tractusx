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
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;

public class PrsUpdateProcessorTests extends PrsIntegrationTestsBase {

    private static final String PATH = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    private static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    private static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    private static final String VIEW = "view";

    private final PrsUpdateEventMother sampleEvents = new PrsUpdateEventMother();

    @Test
    @Disabled
    public void updatePartsAttributes_success() throws JsonProcessingException, InterruptedException {
        //Arrange
        var event = sampleEvents.sampleAttributeUpdateEvent();

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, event.getPart().getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, event.getPart().getObjectIDManufacturer())
                .queryParam(VIEW, PartsTreeView.AS_MAINTAINED)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

            var partRelationships = objectMapper.readValue(response, PartRelationshipsWithInfos.class);


        assertAttributes(event, partRelationships);

    }

    @Test
    @Disabled
    public void updatePartsAspects_success() throws JsonProcessingException {
        //Arrange
        var event = sampleEvents.sampleAspectsUpdateEvent();

        //Act
        publishUpdateEvent(configuration.getPartsAspectsTopic(), event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, event.getPart().getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, event.getPart().getObjectIDManufacturer())
                .queryParam(VIEW, PartsTreeView.AS_MAINTAINED)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        var partRelationships = objectMapper.readValue(response, PartRelationshipsWithInfos.class);

        List<PartInfo> infos = partRelationships.getPartInfos().stream().filter(info -> info.getPart().getObjectIDManufacturer()
                .equals(event.getPart().getObjectIDManufacturer())).collect(Collectors.toList());
        assertThat(infos).hasSize(1);
        PartInfo partInfo = infos.get(0);
        assertThat(partInfo.getPart()).isEqualTo(event.getPart());
        assertThat(partInfo.getAspects()).isEqualTo(event.getAspects());
    }

    @Test
    @Disabled
    public void updatePartsRelationship_success() throws JsonProcessingException {
        //Arrange

        PartRelationshipUpdateEvent.RelationshipUpdate relationshipUpdate = sampleEvents.sampleRelationshipUpdate();
        var event = PartRelationshipUpdateEvent.builder()
                .withRelationships(List.of(relationshipUpdate))
                .build();
        PartRelationship relationship = relationshipUpdate.getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent(configuration.getPartsRelationshipTopic(), event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                .queryParam(VIEW, PartsTreeView.AS_BUILT)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        var partRelationships = objectMapper.readValue(response, PartRelationshipsWithInfos.class);

        List<PartRelationship> infos = partRelationships.getRelationships().stream().filter(rel -> rel.getParent()
                .equals(relationshipUpdate.getRelationship().getParent())).collect(Collectors.toList());
        assertThat(infos).hasSize(1);
        assertThat(partRelationships.getRelationships()).hasSize(1);
        assertThat(partRelationships.getRelationships()).contains(relationship);
    }

    @Test
    @Disabled
    public void updateTwoPartsRelationships_success() throws JsonProcessingException {

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
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, parent1.getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, parent1.getObjectIDManufacturer())
                .queryParam(VIEW, PartsTreeView.AS_BUILT)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        var partRelationships = objectMapper.readValue(response, PartRelationshipsWithInfos.class);

        assertThat(partRelationships.getRelationships()).hasSize(2);
        assertThat(partRelationships.getRelationships())
                .contains(relationship1, relationship2);
    }

    @Test
    @Disabled
    public void updatePartsAttributesWithDifferentEffectTimes_success() throws JsonProcessingException {
        //Arrange
        Instant effectTimeOlderEvent = Instant.now();
        Instant effectTimeEvent = effectTimeOlderEvent.plusSeconds(1);
        var olderEvent = sampleEvents.sampleAttributeUpdateEventWithEffectTime(effectTimeOlderEvent);
        var event = sampleEvents.sampleAttributeUpdateEventWithEffectTime(effectTimeEvent);

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);
        publishUpdateEvent(configuration.getPartsAttributesTopic(), olderEvent);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, event.getPart().getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, event.getPart().getObjectIDManufacturer())
                .queryParam(VIEW, PartsTreeView.AS_BUILT)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        var partRelationships = objectMapper.readValue(response, PartRelationshipsWithInfos.class);

        assertAttributes(event, partRelationships);
    }

    @Test
    @Disabled
    public void sendWrongMassage_success() throws JsonProcessingException {

        //Arrange
        var event = sampleEvents.sampleAttributeUpdateEvent();

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), "wrong_message");
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        var response =
            given()
                .pathParam(ONE_ID_MANUFACTURER, event.getPart().getOneIDManufacturer())
                .pathParam(OBJECT_ID_MANUFACTURER, event.getPart().getObjectIDManufacturer())
                .queryParam(VIEW, PartsTreeView.AS_BUILT)
            .when()
                .get(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();

        var partRelationships = objectMapper.readValue(response, PartRelationshipsWithInfos.class);

        assertAttributes(event, partRelationships);
    }

    private void assertAttributes(PartAttributeUpdateEvent event, PartRelationshipsWithInfos partRelationships) {
        List<PartInfo> infos = partRelationships.getPartInfos().stream().filter(info -> info.getPart().getObjectIDManufacturer()
                .equals(event.getPart().getObjectIDManufacturer())).collect(Collectors.toList());
        assertThat(infos).hasSize(1);
        PartInfo partInfo = infos.get(0);
        assertThat(partInfo.getPart()).isEqualTo(event.getPart());
        assertThat(partInfo.getPartTypeName()).isEqualTo(event.getValue());
    }
    // TODO: add test with effecttime in the future - failure
}
