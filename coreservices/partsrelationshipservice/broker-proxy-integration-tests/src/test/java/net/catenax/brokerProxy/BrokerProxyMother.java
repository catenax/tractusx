// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerProxy;

import com.catenax.partsrelationshipservice.dtos.ErrorResponse;
import com.catenax.partsrelationshipservice.dtos.PartAttribute;
import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.PartRelationship;
import com.catenax.partsrelationshipservice.dtos.events.PartAspectUpdateRequest;
import com.catenax.partsrelationshipservice.dtos.events.PartAttributeUpdateRequest;
import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipUpdate;
import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import net.catenax.brokerproxy.controllers.ApiErrorsConstants;
import net.catenax.prs.testing.DtoMother;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.DAYS;

/**
 * Object Mother to generate data for integration tests.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">
 * https://martinfowler.com/bliki/ObjectMother.html</a>
 */
public class BrokerProxyMother {

    /**
     * JavaFaker instance used to generate random data.
     */
    private final transient Faker faker = new Faker();
    /**
     * Object Mother to generate core DTO data for testing.
     */
    private final transient DtoMother generate = new DtoMother();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BrokerProxyMother() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Generate a {@link PartAspectUpdateRequest} containing random data.
     *
     * @return never returns {@literal null}.
     */
    public PartAspectUpdateRequest partAspectUpdate() {
        return PartAspectUpdateRequest.builder()
                .withPart(generate.partId())
                .withAspects(singletonList(generate.partAspect()))
                .withRemove(false)
                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                .build();
    }

    /**
     * Generate a PartAspectUpdateRequest json with empty aspects list.
     *
     * @return never returns {@literal null}.
     */
    public String partAspectUpdateEmptyList() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partAspectUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.replace("aspects", objectMapper.createArrayNode());
        return objectNode.toString();

    }

    /**
     * Generate a PartAspectUpdateRequest json with no part id.
     *
     * @return never returns {@literal null}.
     */
    public String partAspectUpdateNoPartId() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partAspectUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.remove("part");
        return objectNode.toString();

    }

    /**
     * Generate a PartAspectUpdateRequest json with no effectTime.
     *
     * @return never returns {@literal null}.
     */
    public String partAspectUpdateNoEffectTime() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partAspectUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.remove("effectTime");
        return objectNode.toString();

    }

    /**
     * Generate a {@link PartAttributeUpdateRequest} containing random data.
     *
     * @return never returns {@literal null}.
     */
    public PartAttributeUpdateRequest partAttributeUpdate() {
        return PartAttributeUpdateRequest.builder()
                .withPart(generate.partId())
                .withName(faker.options().option(PartAttribute.class).name())
                .withValue(faker.commerce().productName())
                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                .build();
    }

    /**
     * Generate a PartAttributeUpdateRequest json with wrong PartAttributeName.
     *
     * @return never returns {@literal null}.
     */
    public String partAttributeUpdateWrongName() throws com.fasterxml.jackson.core.JsonProcessingException {

        var  request = objectMapper.writeValueAsString(partAttributeUpdate());

        return request.replace(PartAttribute.PART_TYPE_NAME.name(), "fake_attribute_name");

    }

    /**
     * Generate a PartAttributeUpdateRequest json without effectTime.
     *
     * @return never returns {@literal null}.
     */
    public String partAttributeUpdateNoEffectTime() throws com.fasterxml.jackson.core.JsonProcessingException {

        var  request = objectMapper.writeValueAsString(partAttributeUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.remove("effectTime");
        return objectNode.toString();

    }

    /**
     * Generate a PartAttributeUpdateRequest json without attribute value.
     *
     * @return never returns {@literal null}.
     */
    public String partAttributeUpdateNoAttributeValue() throws com.fasterxml.jackson.core.JsonProcessingException {

        var  request = objectMapper.writeValueAsString(partAttributeUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.remove("value");
        return objectNode.toString();

    }

    /**
     * Generate a PartAttributeUpdateRequest json without part id.
     *
     * @return never returns {@literal null}.
     */
    public String partAttributeUpdateNoPartId() throws com.fasterxml.jackson.core.JsonProcessingException {

        var  request = objectMapper.writeValueAsString(partAttributeUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.remove("part");
        return objectNode.toString();

    }

    /**
     * Generates error response for invalid arguments provided scenario.
     * @param errors List of errors.
     * @return An {@link ErrorResponse} object containing list of supplied errors.
     */
    public ErrorResponse invalidArgument(List<String> errors) {
        return ErrorResponse.builder()
                .withStatusCode(HttpStatus.BAD_REQUEST)
                .withMessage(ApiErrorsConstants.INVALID_ARGUMENTS)
                .withErrors(errors).build();
    }

    /**
     * Generate a PartRelationshipUpdateRequest.
     *
     * @return never returns {@literal null}.
     */
    public PartRelationshipUpdateRequest partRelationshipUpdate() {
        return PartRelationshipUpdateRequest.builder()
                .withRelationships(List.of(PartRelationshipUpdate.builder()
                                .withRelationship(PartRelationship.builder()
                                        .withChild(generate.partId())
                                        .withParent(generate.partId())
                                        .build())
                                .withRemove(false)
                                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                                .withStage(PartLifecycleStage.BUILD)
                        .build()))
                .build();
    }

    /**
     * Generate a PartRelationshipUpdateRequest json with empty relationships list.
     *
     * @return never returns {@literal null}.
     */
    public String partRelationshipUpdateNoRelationships() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partRelationshipUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        objectNode.replace("relationships", objectMapper.createArrayNode());
        return objectNode.toString();
    }

    /**
     * Generate a PartRelationshipUpdateRequest json without effectTime.
     *
     * @return never returns {@literal null}.
     */
    public String partRelationshipUpdateNoEffectTime() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partRelationshipUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        ArrayNode arrayNode = (ArrayNode) objectNode.get("relationships");
        for(int i = 0; i < arrayNode.size(); i++) {
            ObjectNode arrayElement = (ObjectNode) arrayNode.get(i);
            arrayElement.remove("effectTime");
        }
        objectNode.replace("relationships", arrayNode);
        return objectNode.toString();
    }

    /**
     * Generate a PartRelationshipUpdateRequest json without stage.
     *
     * @return never returns {@literal null}.
     */
    public String partRelationshipUpdateNoStage() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partRelationshipUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        ArrayNode arrayNode = (ArrayNode) objectNode.get("relationships");
        for(int i = 0; i < arrayNode.size(); i++) {
            ObjectNode arrayElement = (ObjectNode) arrayNode.get(i);
            arrayElement.remove("stage");
        }
        objectNode.replace("relationships", arrayNode);
        return objectNode.toString();
    }

    /**
     * Generate a PartRelationshipUpdateRequest json with not valid stage.
     *
     * @return never returns {@literal null}.
     */
    public String partRelationshipUpdateInvalidStage() throws JsonProcessingException {
        var request = objectMapper.writeValueAsString(partRelationshipUpdate());

        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(request);
        ArrayNode arrayNode = (ArrayNode) objectNode.get("relationships");
        for(int i = 0; i < arrayNode.size(); i++) {
            ObjectNode arrayElement = (ObjectNode) arrayNode.get(i);
            arrayElement.put("stage", "invalidStage");
        }
        objectNode.replace("relationships", arrayNode);
        return objectNode.toString();
    }
}
