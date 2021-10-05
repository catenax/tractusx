package net.catenax.brokerProxy;

import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import net.catenax.brokerproxy.requests.PartRelationshipUpdateRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdatePartRelationshipTest extends BrokerProxyIntegrationTestBase {

    private static final String PATH = "/brokerproxy/v0.1/PartRelationshipUpdateList";

    @Test
    public void updatedPartsRelationships_success() {

        var updateRequest = brokerProxyMother.partRelationshipUpdate();

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

    @SneakyThrows
    private boolean hasExpectedBrokerEvent(PartRelationshipUpdateRequest request) {
        var consumer = subscribe(configuration.getPartsRelationshipTopic());
        Instant afterTenSeconds = Instant.now().plusSeconds(10);
        boolean isEventMatched = false;
        while (Instant.now().isBefore(afterTenSeconds)) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {

                if(record.value()!= null) {
                    PartRelationshipUpdateEvent event = objectMapper.readValue(record.value(), PartRelationshipUpdateEvent.class);

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

    private boolean isEqual(PartRelationshipUpdateRequest request, PartRelationshipUpdateEvent event) {

        for (var relInRequest : request.getRelationships()) {
            boolean isMatched = false;
            for(var relInEvent : event.getRelationships()) {
                if(relInRequest.getRelationship().equals(relInEvent.getRelationship())
                        && relInRequest.getStage().equals(relInEvent.getStage())
                        && relInRequest.getEffectTime().equals(relInEvent.getEffectTime())
                        && relInRequest.isRemove()== relInRequest.isRemove()) {
                    isMatched = true;
                    break;
                }
            }
            if(!isMatched) {
                return false;
            }
        }

        return true;
    }

}
