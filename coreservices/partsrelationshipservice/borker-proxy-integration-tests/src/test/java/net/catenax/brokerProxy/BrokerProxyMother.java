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
import com.catenax.partsrelationshipservice.dtos.PartAttributeName;
import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.PartRelationship;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import net.catenax.brokerproxy.controllers.ApiErrorsConstants;
import net.catenax.brokerproxy.requests.PartAspectUpdateRequest;
import net.catenax.brokerproxy.requests.PartAttributeUpdateRequest;
import net.catenax.brokerproxy.requests.PartRelationshipUpdate;
import net.catenax.brokerproxy.requests.PartRelationshipUpdateRequest;
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
     * Generate a {@link PartAttributeUpdateRequest} containing random data.
     *
     * @return never returns {@literal null}.
     */
    public PartAttributeUpdateRequest partAttributeUpdate() {
        return PartAttributeUpdateRequest.builder()
                .withPart(generate.partId())
                .withName(faker.options().option(PartAttributeName.class).name())
                .withValue(faker.commerce().productName())
                .withEffectTime(faker.date().past(100, DAYS).toInstant())
                .build();
    }

    /**
     * Generate a {@link PartAttributeUpdateRequest} containing random data without attributes name.
     *
     * @return never returns {@literal null}.
     */
    public String partAttributeUpdateWrongName() throws com.fasterxml.jackson.core.JsonProcessingException {

        var  request = objectMapper.writeValueAsString(PartAttributeUpdateRequest.builder()
                .withPart(generate.partId())
                .withName(PartAttributeName.PART_TYPE_NAME.name())
                .withValue(faker.commerce().productName())
                .withEffectTime(faker.date().past(100, DAYS).toInstant()).build());

        return request.replace(PartAttributeName.PART_TYPE_NAME.name(), "fake_attribute_name");

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
}
