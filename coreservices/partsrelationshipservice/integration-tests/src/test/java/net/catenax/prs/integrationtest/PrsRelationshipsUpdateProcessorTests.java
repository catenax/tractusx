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

import com.catenax.partsrelationshipservice.dtos.PartId;
import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.PartRelationship;
import com.catenax.partsrelationshipservice.dtos.PartRelationshipsWithInfos;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import com.github.javafaker.Faker;
import net.catenax.prs.testing.DtoMother;
import net.catenax.prs.testing.PartUpdateEventMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_BUILT;
import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class PrsRelationshipsUpdateProcessorTests extends PrsIntegrationTestsBase {

    private final PartUpdateEventMother generate = new PartUpdateEventMother();
    private final DtoMother generateDto = new DtoMother();

    @Test
    public void updatePartsRelationship_success() throws Exception {

        //Arrange
        var relationshipUpdate = generateAddedRelationship();
        var event = generate.relationshipUpdateEvent(relationshipUpdate);
        var relationship = relationshipUpdate.getRelationship();
        var parent = relationship.getParent();

        //Act
        publishUpdateEvent(event);

        //Assert
        await().untilAsserted(() -> {
            var response =
                    given()
                            .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                            .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                            .queryParam(VIEW, AS_BUILT)
                            .when()
                            .get(PATH)
                            .then()
                            .assertThat()
                            .statusCode(SC_OK)
                            .extract().as(PartRelationshipsWithInfos.class);

            assertThat(response.getRelationships()).containsExactly(relationship);
        });
    }

    @Test
    public void updateTwoPartsRelationships_success() throws Exception {

        //Arrange
        //Create two relationships with same parent
        var update1 = generateAddedRelationship();
        var update2 = generateAddedRelationship()
                .toBuilder()
                .withRelationship(generateDto.partRelationship().toBuilder().withParent(update1.getRelationship().getParent()).build())
                .build();

        var event = generate.relationshipUpdateEvent(update1, update2);
        PartRelationship relationship1 = update1.getRelationship();
        PartRelationship relationship2 = update2.getRelationship();
        PartId parent1 = relationship1.getParent();

        //Act
        publishUpdateEvent(event);

        //Assert
        await().untilAsserted(() -> {
            var response =
                    given()
                        .pathParam(ONE_ID_MANUFACTURER, parent1.getOneIDManufacturer())
                        .pathParam(OBJECT_ID_MANUFACTURER, parent1.getObjectIDManufacturer())
                        .queryParam(VIEW, AS_BUILT)
                    .when()
                        .get(PATH)
                    .then()
                        .assertThat()
                        .statusCode(SC_OK)
                        .extract().as(PartRelationshipsWithInfos.class);

            assertThat(response.getRelationships())
                    .containsExactlyInAnyOrder(relationship1, relationship2);
        });
    }

    /**
     * An invalid message payload is sent to the dead-letter topic rather than blocking processing,
     * so that a subsequent message in the same partition is processed correctly.
     *
     * @param name test case name, used to generate test display name
     * @param invalidPayload the invalid payload to send before a valid payload
     * @throws Exception on failure
     */
    @ParameterizedTest(name = "{index} {0}")
    @ArgumentsSource(BlankStringsArgumentsProvider.class)
    public void sendWrongMessageThenCorrectMessage_success(String name, Object invalidPayload) throws Exception {

        //Arrange
        var relationshipUpdate = generateAddedRelationship();
        var event = generate.relationshipUpdateEvent(relationshipUpdate);
        PartRelationship relationship = relationshipUpdate.getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent(invalidPayload);
        publishUpdateEvent(event);

        //Assert
        await().atMost(180, SECONDS).untilAsserted(() -> {
            var response =
                    given()
                        .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                        .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                        .queryParam(VIEW, AS_BUILT)
                    .when()
                        .get(PATH)
                    .then()
                        .assertThat()
                        .statusCode(SC_OK)
                        .extract().as(PartRelationshipsWithInfos.class);

            assertThat(response.getRelationships()).containsExactly(relationship);
        });
    }

    @Test
    public void updatePartsRelationshipsDuplicateEvent_success() throws Exception {

        //Arrange
        var relationshipUpdate = generateAddedRelationship();
        var event = generate.relationshipUpdateEvent(relationshipUpdate);
        PartRelationship relationship = relationshipUpdate.getRelationship();
        PartId parent = relationship.getParent();

        //Act
        publishUpdateEvent(event);
        publishUpdateEvent(event);

        //Assert
        await().untilAsserted(() -> {
            var response =
                    given()
                        .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                        .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                        .queryParam(VIEW, AS_BUILT)
                    .when()
                        .get(PATH)
                    .then()
                        .assertThat()
                        .statusCode(SC_OK)
                        .extract().as(PartRelationshipsWithInfos.class);

            assertThat(response.getRelationships()).containsExactly(relationship);
        });
    }

    private PartRelationshipUpdateEvent.RelationshipUpdate generateAddedRelationship() {
        return generate.relationshipUpdate()
                .toBuilder()
                .withRemove(false)
                .withStage(PartLifecycleStage.BUILD)
                .build();
    }

    static class BlankStringsArgumentsProvider implements ArgumentsProvider {

        private final PartUpdateEventMother generate = new PartUpdateEventMother();

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            final var invalidUpdate = generate.relationshipUpdate()
                    .toBuilder()
                    .withEffectTime(null)
                    .build();
            return Stream.of(
                    Arguments.of("unsupported payload type", new Faker().lorem().sentence()),
                    Arguments.of("invalid payload", generate.relationshipUpdateEvent(invalidUpdate))
            );
        }
    }
}
