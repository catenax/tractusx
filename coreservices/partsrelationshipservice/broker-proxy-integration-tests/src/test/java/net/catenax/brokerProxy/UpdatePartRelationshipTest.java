package net.catenax.brokerProxy;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import net.catenax.prs.dtos.events.PartRelationshipUpdate;
import net.catenax.prs.dtos.events.PartRelationshipsUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;

import java.time.Instant;
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

    @ParameterizedTest
    @NullAndEmptySource
    public void updatedPartsAttributesWithNoRelationships_failure(List<PartRelationshipUpdate> relationships) {

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
                .isEqualTo(generateResponse.invalidArgument(List.of("relationships:must not be empty")));
    }

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("provideInvalidEffectTime")
    public void updatedPartsAttributesWithInvalidEffectTime_failure(String name, Instant effectTime, String expectedError) {

        var response =
            given()
                .contentType(ContentType.JSON)
                .body(getPartRelationshipUpdateRequest(s -> s.withEffectTime(effectTime)))
            .when()
                .post(PATH)
            .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertThatJson(response)
                .isEqualTo(generateResponse.invalidArgument(List.of(expectedError)));
    }


    protected static Stream<Arguments> provideInvalidEffectTime() {
        Faker faker = new Faker();
        return Stream.of(
                Arguments.of("Null effectTime", null, "relationships[0].effectTime:must not be null"),
                Arguments.of("Future effectTime", faker.date().future(faker.number().randomDigitNotZero(), TimeUnit.DAYS).toInstant()
                        , "relationships[0].effectTime:must be a past date")
        );
    }

    private PartRelationshipsUpdateRequest getPartRelationshipUpdateRequest(Function<PartRelationshipUpdate.PartRelationshipUpdateBuilder, PartRelationshipUpdate.PartRelationshipUpdateBuilder> f) {
        var event = generate.partRelationshipUpdateList();
        List<PartRelationshipUpdate> relationships = event.getRelationships().stream().map(r -> f.apply(r.toBuilder()).build()).collect(Collectors.toList());
        return event.toBuilder().withRelationships(relationships).build();
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
}
