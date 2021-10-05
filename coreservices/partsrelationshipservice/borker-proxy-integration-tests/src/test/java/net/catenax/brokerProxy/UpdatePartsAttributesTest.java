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

import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import net.catenax.brokerproxy.requests.PartAttributeUpdateRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import java.time.Duration;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;

public class UpdatePartsAttributesTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/brokerproxy/v0.1/PartAttributeUpdate";

    @Test
    public void updatedPartsAttributes_success() {

        var updateRequest = brokerProxyMother.partAttributeUpdate();

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
        .when()
                .post(PATH)
        .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(hasExpectedBrokerEvent(updateRequest)).isTrue();
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

    @Test
    public void updatedPartsAttributesWrongAttributeName_failure() throws JsonProcessingException {

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(brokerProxyMother.partAttributeUpdateWrongName())
                .when()
                    .post(PATH)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract().asString();

        assertThatJson(response)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(brokerProxyMother.invalidArgument(List.of("name:Invalid attribute name.")));
    }

    @SneakyThrows
    private boolean hasExpectedBrokerEvent(PartAttributeUpdateRequest request) {
        var consumer = subscribe(configuration.getPartsAttributesTopic());
        Instant afterTenSeconds = Instant.now().plusSeconds(10);
        boolean isEventMatched = false;
        while (Instant.now().isBefore(afterTenSeconds)) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {

                if(record.value()!= null) {
                    PartAttributeUpdateEvent event = objectMapper.readValue(record.value(), PartAttributeUpdateEvent.class);

                    if(isEqual(request, event)){
                        isEventMatched = true;
                        break;
                    }
                }
            }
        }

        consumer.close();

        return isEventMatched;
    }

    private boolean isEqual(PartAttributeUpdateRequest request, PartAttributeUpdateEvent event) {
        return event.getPart().equals(request.getPart())
                && event.getEffectTime().equals(request.getEffectTime())
                && event.getName().equals(request.getName())
                && event.getValue().equals(request.getValue());
    }

}
