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
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static io.restassured.RestAssured.given;

public class PrsUpdateProcessorTests extends PrsIntegrationTestsBase{

    private static final String PATH = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    private static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    private static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    private static final String VIEW = "view";

    private final PrsUpdateEventMother sampleEvents = new PrsUpdateEventMother();

    @Test
    @Disabled
    public void updatePartsAttributes_success() throws JsonProcessingException {
        //Arrange
        var event = sampleEvents.sampleAttributeUpdateEvent();

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        var response = given()
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

        assertThat(partRelationships.getPartInfos()).hasSize(1);
        PartInfo partInfo = partRelationships.getPartInfos().get(0);
        assertThat(partInfo.getPart()).isEqualTo(event.getPart());
        assertThat(partInfo.getPartTypeName()).isEqualTo(event.getValue());
    }

    @Test
    @Disabled
    public void updatePartsAspects_success() throws JsonProcessingException {
        //Arrange
        var event = sampleEvents.sampleAspectsUpdateEvent();

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        var response = given()
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

        assertThat(partRelationships.getPartInfos()).hasSize(1);
        PartInfo partInfo = partRelationships.getPartInfos().get(0);
        assertThat(partInfo.getPart()).isEqualTo(event.getPart());
        assertThat(partInfo.getAspects()).isEqualTo(event.getAspects());
    }

    @Test
    @Disabled
    public void updatePartsRelationship_success() throws JsonProcessingException {
        //Arrange
        var event = sampleEvents.sampleRelationhsipUpdateEvent();
        PartRelationship relationship = event.getRelationships().get(0).getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        var response = given()
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

        assertThat(partRelationships.getRelationships()).hasSize(1);
        PartRelationship partRelationship = partRelationships.getRelationships().get(0);
        assertThat(partRelationship.getParent()).isEqualTo(parent);
        assertThat(partRelationship.getChild()).isEqualTo(relationship.getChild());
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
        var response = given()
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

        assertThat(partRelationships.getPartInfos()).hasSize(1);
        PartInfo partInfo = partRelationships.getPartInfos().get(0);
        assertThat(partInfo.getPart()).isEqualTo(event.getPart());
        assertThat(partInfo.getPartTypeName()).isEqualTo(event.getValue());
    }

    @Test
    public void sendWrongMassage_failure() {

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), "wrong_message");
    }
}
