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

import com.catenax.partsrelationshipservice.dtos.events.PartAspectsUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.events.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.events.PartRelationshipsUpdateEvent;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
     * in uploaded {@link PartRelationshipsUpdateEvent} messages.
     */
    private DistributionSummary uploadedBomSize;

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
     * Send a {@link PartRelationshipsUpdateEvent} to the broker.
     *
     * @param updateRelationships message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartRelationshipsUpdateEvent updateRelationships) {
        uploadedBomSize.record(updateRelationships.getRelationships().size());

        log.info("Sending PartRelationshipUpdateList to broker");
        producerService.send(updateRelationships);
        log.info("Sent PartRelationshipUpdateList to broker");
    }

    /**
     * Send a {@link PartAspectsUpdateEvent} to the broker.
     *
     * @param updateAspect message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartAspectsUpdateEvent updateAspect) {
        log.info("Sending PartAspectUpdate to broker");
        producerService.send(updateAspect);
        log.info("Sent PartAspectUpdate to broker");
    }

    /**
     * Send a {@link PartAttributeUpdateEvent} to the broker.
     *
     * @param updateAttribute message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartAttributeUpdateEvent updateAttribute) {
        log.info("Sending PartAttributeUpdate to broker");
        producerService.send(updateAttribute);
        log.info("Sent PartAttributeUpdate to broker");
    }
}
