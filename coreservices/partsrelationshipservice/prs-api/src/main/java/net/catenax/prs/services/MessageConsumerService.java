//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka message consumer service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${prs.kafkaTopic}")
public class MessageConsumerService {

    /**
     * Service for processing parts tree update events.
     */
    private final PartsTreeUpdateProcessorService updateProcessorService;

    /**
     * Kafka consumer for prs data update events.
     *
     * @param payload PRS data update event from broker.
     */
    @KafkaHandler
    public void consumePartRelationshipUpdateEvent(final PartRelationshipUpdateEvent payload) {
        log.info("PartRelationshipUpdateEvent event received.");
        updateProcessorService.update(payload);
        log.info("Event processed.");
    }
}
