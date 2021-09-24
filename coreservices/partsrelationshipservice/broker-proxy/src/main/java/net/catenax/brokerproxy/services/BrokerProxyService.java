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

import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateListMessage;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.messaging.KafkaSettings;
import org.springframework.kafka.core.KafkaOperations;
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
     * Object allowing to send messages to the Kafka broker.
     */
    private final KafkaOperations<String, Object> kafka;
    /**
     * Registry for publishing custom metrics.
     */
    private final MeterRegistry registry;
    /**
     * Kafka configuration.
     */
    private final KafkaSettings kafkaSettings;

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
     */
    public void uploadPartRelationshipUpdateList(final PartRelationshipUpdateList updateList) {
        uploadedBomSize.record(updateList.getRelationships().size());

        log.info("Sending PartRelationshipUpdateList to broker");
        final var message = PartRelationshipUpdateListMessage.builder()
                .withPartRelationshipUpdateListId(UUID.randomUUID())
                .withPayload(updateList)
                .build();
        kafka.send(kafkaSettings.getTopic(), message);
        log.info("Sent PartRelationshipUpdateList to broker");
    }
}
