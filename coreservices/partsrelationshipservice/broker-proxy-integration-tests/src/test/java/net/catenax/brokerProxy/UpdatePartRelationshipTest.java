package net.catenax.brokerProxy;

import io.restassured.http.ContentType;
import net.catenax.prs.dtos.events.PartRelationshipUpdate;
import net.catenax.prs.dtos.events.PartRelationshipsUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartRelationshipTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/broker-proxy/v0.1/partRelationshipUpdateList";

    @Test
    public void updatedPartsRelationships_success() throws Exception {

        var event = generate.partRelationshipUpdateList();

        given()
            .contentType(ContentType.JSON)
            .body(event)
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(event, PartRelationshipsUpdateRequest.class)).isTrue();
    }

    @Test
    public void updatedPartsAttributesBadRequest_failure() {

        given()
            .contentType(ContentType.JSON)
            .body("bad request")
        .when()
            .post(PATH)
        .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideInvalidRelationships")
    public void updatedPartsAttributesWithInvalidRelationships_failure(List<PartRelationshipUpdate> relationships, String expectedError) {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(generate.partRelationshipUpdateList().toBuilder().withRelationships(relationships).build())
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of(expectedError)));
    }

    @Test
    public void updatedPartsAttributesWithNoEffectTime_failure() {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(getPartRelationshipUpdateRequest(s -> s.withEffectTime(null)))
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of("relationships[0].effectTime:must not be null")));
    }

    @Test
    public void updatedPartsAttributesWithFutureEffectTime_failure() {

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(getPartRelationshipUpdateRequest(s
                                -> s.withEffectTime(faker.date().future(faker.number().randomDigitNotZero(), TimeUnit.DAYS)
                                .toInstant())))
                .when()
                        .post(PATH)
                .then()
                        .assertThat()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of("relationships[0].effectTime:must be a past date")));
    }

    @Test
    public void updatedPartsAttributesWithNoStage_failure() {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(getPartRelationshipUpdateRequest(s -> s.withStage(null)))
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of("relationships[0].stage:must not be null")));
    }

    private PartRelationshipsUpdateRequest getPartRelationshipUpdateRequest(Function<PartRelationshipUpdate.PartRelationshipUpdateBuilder, PartRelationshipUpdate.PartRelationshipUpdateBuilder> f) {
        var event = generate.partRelationshipUpdateList();
        List<PartRelationshipUpdate> relationships = event.getRelationships().stream().map(r -> f.apply(r.toBuilder()).build()).collect(Collectors.toList());
        return event.toBuilder().withRelationships(relationships).build();
    }

    /**
     * Provides invalid aspects test data.
     * @return Invalid aspects as {@link Stream} of {@link Arguments}.
     */
    private static Stream<Arguments> provideInvalidRelationships() {
        return Stream.of(
                Arguments.of(null, "relationships:must not be empty"),
                Arguments.of(Collections.emptyList(), "relationships:must not be empty"),
                Arguments.of(List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(null)
                                .build())
                        .build()), "relationships[0].relationship.child:must not be null"),
                Arguments.of(List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withParent(null)
                                .build())
                        .build()), "relationships[0].relationship.parent:must not be null"),
                Arguments.of(List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(null)
                                        .build())
                                .build())
                        .build()), "relationships[0].relationship.child.oneIDManufacturer:must not be blank"),
                Arguments.of(List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(generateDto.partId().toBuilder()
                                        .withObjectIDManufacturer(null)
                                        .build())
                                .build())
                        .build()), "relationships[0].relationship.child.objectIDManufacturer:must not be blank"),
                Arguments.of(List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withParent(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(null)
                                        .build())
                                .build())
                        .build()), "relationships[0].relationship.parent.oneIDManufacturer:must not be blank"),
                Arguments.of(List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withParent(generateDto.partId().toBuilder()
                                        .withObjectIDManufacturer(null)
                                        .build())
                                .build())
                        .build()), "relationships[0].relationship.parent.objectIDManufacturer:must not be blank")
        );
    }
}
