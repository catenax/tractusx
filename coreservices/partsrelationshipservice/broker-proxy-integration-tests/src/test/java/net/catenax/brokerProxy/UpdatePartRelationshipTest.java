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
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static net.catenax.prs.dtos.ValidationConstants.INPUT_FIELD_MAX_LENGTH;
import static net.catenax.prs.dtos.ValidationConstants.RELATIONSHIP_UPDATE_LIST_MAX_SIZE;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
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

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("provideInvalidRelationships")
    public void updatedPartsAttributesWithInvalidRelationships_failure(String name, List<PartRelationshipUpdate> relationships, List<String> expectedErrors) {

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
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(generateResponse.invalidArgument(expectedErrors));
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
     * Provides invalid relationships test data.
     * @return Invalid relationships as {@link Stream} of {@link Arguments}.
     */
    private static Stream<Arguments> provideInvalidRelationships() {
        return Stream.of(
                Arguments.of("Null relationship",
                        null,
                        List.of("relationships:must not be empty")),
                Arguments.of("Empty relationship",
                        Collections.emptyList(),
                        List.of("relationships:must not be empty",
                        "relationships:size must be between 1 and 1000")),
                Arguments.of("Too many relationships",
                        IntStream.rangeClosed(0, RELATIONSHIP_UPDATE_LIST_MAX_SIZE).mapToObj(i -> generate.partRelationshipUpdate())
                                .collect(Collectors.toList()),
                        List.of("relationships:size must be between 1 and 1000")),
                Arguments.of("Relationship with parent and child as null",
                        List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(null)
                                .withParent(null)
                                .build())
                        .build()),
                        List.of("relationships[0].relationship.child:must not be null",
                        "relationships[0].relationship.parent:must not be null")),
                Arguments.of("Relationship with child part OneID and ObjectID as null",
                        List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(null)
                                        .withObjectIDManufacturer(null)
                                        .build())
                                .build())
                        .build()),
                        List.of("relationships[0].relationship.child.oneIDManufacturer:must not be blank",
                        "relationships[0].relationship.child.objectIDManufacturer:must not be blank")),
                Arguments.of("Relationship with parent part OneID and ObjectID as null",
                        List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withParent(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(null)
                                        .withObjectIDManufacturer(null)
                                        .build())
                                .build())
                        .build()),
                        List.of("relationships[0].relationship.parent.oneIDManufacturer:must not be blank",
                        "relationships[0].relationship.parent.objectIDManufacturer:must not be blank")),
                Arguments.of("Relationship with parent and child part OneID and ObjectID as empty",
                        List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(EMPTY)
                                        .withObjectIDManufacturer(EMPTY)
                                        .build())
                                .withParent(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(EMPTY)
                                        .withObjectIDManufacturer(EMPTY)
                                        .build())
                                .build())
                        .build()),
                        List.of("relationships[0].relationship.parent.objectIDManufacturer:size must be between 1 and 10000",
                        "relationships[0].relationship.parent.oneIDManufacturer:must not be blank",
                        "relationships[0].relationship.parent.objectIDManufacturer:must not be blank",
                        "relationships[0].relationship.parent.oneIDManufacturer:size must be between 1 and 10000",
                        "relationships[0].relationship.child.objectIDManufacturer:size must be between 1 and 10000",
                        "relationships[0].relationship.child.oneIDManufacturer:must not be blank",
                        "relationships[0].relationship.child.objectIDManufacturer:must not be blank",
                        "relationships[0].relationship.child.oneIDManufacturer:size must be between 1 and 10000")),
                Arguments.of("Relationship with parent and child part OneID and ObjectID as whitespace",
                        List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(faker.regexify(WHITESPACE_REGEX))
                                        .withObjectIDManufacturer(faker.regexify(WHITESPACE_REGEX))
                                        .build())
                                .withParent(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(faker.regexify(WHITESPACE_REGEX))
                                        .withObjectIDManufacturer(faker.regexify(WHITESPACE_REGEX))
                                        .build())
                                .build())
                        .build()),
                        List.of("relationships[0].relationship.parent.oneIDManufacturer:must not be blank",
                        "relationships[0].relationship.parent.objectIDManufacturer:must not be blank",
                        "relationships[0].relationship.child.oneIDManufacturer:must not be blank",
                        "relationships[0].relationship.child.objectIDManufacturer:must not be blank")),
                Arguments.of("Relationship with parent and child part OneID and ObjectID too long",
                        List.of(generate.partRelationshipUpdate().toBuilder()
                        .withRelationship(generateDto.partRelationship().toBuilder()
                                .withChild(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(faker.lorem().characters(INPUT_FIELD_MAX_LENGTH + 1))
                                        .withObjectIDManufacturer(faker.lorem().characters(INPUT_FIELD_MAX_LENGTH + 1))
                                        .build())
                                .withParent(generateDto.partId().toBuilder()
                                        .withOneIDManufacturer(faker.lorem().characters(INPUT_FIELD_MAX_LENGTH + 1))
                                        .withObjectIDManufacturer(faker.lorem().characters(INPUT_FIELD_MAX_LENGTH + 1))
                                        .build())
                                .build())
                        .build()),
                        List.of("relationships[0].relationship.parent.oneIDManufacturer:size must be between 1 and 10000",
                        "relationships[0].relationship.parent.objectIDManufacturer:size must be between 1 and 10000",
                        "relationships[0].relationship.child.oneIDManufacturer:size must be between 1 and 10000",
                        "relationships[0].relationship.child.objectIDManufacturer:size must be between 1 and 10000"))
        );
    }
}
