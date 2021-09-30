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

import com.catenax.partsrelationshipservice.dtos.PartAspectUpdate;
import com.catenax.partsrelationshipservice.dtos.PartAttributeUpdate;
import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateListMessage;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.exceptions.MessageProducerFailedException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

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
     * in uploaded {@link PartRelationshipUpdateList} messages.
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
     * Send a {@link PartRelationshipUpdateList} to the broker.
     *
     * @param updateList message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartRelationshipUpdateList updateList) {
        uploadedBomSize.record(updateList.getRelationships().size());

        log.debug("Sending PartRelationshipUpdateList to broker");
        final var message = PartRelationshipUpdateListMessage.builder()
                .withPartRelationshipUpdateListId(UUID.randomUUID())
                .withPayload(updateList)
                .build();
        producerService.send(message);
        log.debug("Sent PartRelationshipUpdateList to broker");
    }

    /**
     * Send a {@link PartAspectUpdate} to the broker.
     *
     * @param data message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartAspectUpdate data) {
        log.debug("Sending PartAspectUpdate to broker");
        producerService.send(data);
        log.debug("Sent PartAspectUpdate to broker");
    }

    /**
     * Send a {@link PartAttributeUpdate} to the broker.
     *
     * @param data message to send.
     * @throws MessageProducerFailedException if message could not be delivered to the broker.
     */
    public void send(final PartAttributeUpdate data) {
        log.debug("Sending PartAttributeUpdate to broker");
        producerService.send(data);
        log.debug("Sent PartAttributeUpdate to broker");
    }
}
