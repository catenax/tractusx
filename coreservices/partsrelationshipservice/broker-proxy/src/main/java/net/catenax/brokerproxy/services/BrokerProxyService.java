//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerproxy.services;

import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.configuration.BrokerProxyConfiguration;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import net.catenax.brokerproxy.requests.PartAspectUpdateRequest;
import net.catenax.brokerproxy.requests.PartAttributeUpdateRequest;
import net.catenax.brokerproxy.requests.PartRelationshipUpdateRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

/**
 * Broker proxy service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerProxyService {

    /**
     * Kafka message producer service.
     */
    private final MessageProducerService producerService;
    /**
     * Registry for publishing custom metrics.
     */
    private final MeterRegistry registry;

    /**
     * A custom metric recording the number of items
     * in uploaded {@link PartRelationshipUpdateRequest} messages.
     */
    private DistributionSummary uploadedBomSize;

    /**
     * Kafka configuration.
     */
    private final BrokerProxyConfiguration configuration;

    /**
     * Initialize custom metric.
     */
    @PostConstruct
    public void initialize() {
        uploadedBomSize = DistributionSummary
                .builder("uploaded_bom_size")
                .description("Number of items in uploaded PartRelationshipUpdateList")
                .register(registry);
    }

    /**
     * Send a {@link PartRelationshipUpdateRequest} to the broker.
     *
     * @param updateRelationships message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartRelationshipUpdateRequest updateRelationships) {
        uploadedBomSize.record(updateRelationships.getRelationships().size());

        log.info("Sending PartRelationshipUpdateList to broker");
        final var relationshipsToUpdate = updateRelationships.getRelationships()
                .stream().map(rel -> PartRelationshipUpdateEvent.RelationshipUpdate.builder()
                                .withRelationship(rel.getRelationship())
                                .withStage(rel.getStage())
                                .withRemove(rel.isRemove())
                                .withEffectTime(rel.getEffectTime())
                                .build())
                .collect(Collectors.toList());

        final var message = PartRelationshipUpdateEvent.builder()
                        .withRelationships(relationshipsToUpdate)
                .build();
        producerService.send(configuration.getPartsRelationshipTopic(), message);
        log.info("Sent PartRelationshipUpdateList to broker");
    }

    /**
     * Send a {@link PartAspectUpdateRequest} to the broker.
     *
     * @param updateAspect message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartAspectUpdateRequest updateAspect) {
        log.info("Sending PartAspectUpdate to broker");
        final var message = PartAspectUpdateEvent.builder()
                .withPart(updateAspect.getPart())
                .withAspects(updateAspect.getAspects())
                .withRemove(updateAspect.isRemove())
                .withEffectTime(updateAspect.getEffectTime())
                .build();
        producerService.send(configuration.getPartsAspectsTopic(), message);
        log.info("Sent PartAspectUpdate to broker");
    }

    /**
     * Send a {@link PartAttributeUpdateRequest} to the broker.
     *
     * @param updateAttribute message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartAttributeUpdateRequest updateAttribute) {
        log.info("Sending PartAttributeUpdate to broker");
        final var message = PartAttributeUpdateEvent.builder()
                .withPart(updateAttribute.getPart())
                .withName(updateAttribute.getName())
                .withValue(updateAttribute.getValue())
                .withEffectTime(updateAttribute.getEffectTime())
                .build();
        producerService.send(configuration.getPartsAttributesTopic(), message);
        log.info("Sent PartAttributeUpdate to broker");
    }
}
