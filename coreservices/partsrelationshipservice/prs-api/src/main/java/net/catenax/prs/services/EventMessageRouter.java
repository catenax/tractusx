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

import javax.validation.Valid;

/**
 * Kafka message consumer service, routing event messages by payload type.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${prs.kafkaTopic}")
public class EventMessageRouter {

    /**
     * Service for processing {@link PartRelationshipUpdateEvent}s.
     */
    private final PartRelationshipUpdateProcessor updateProcessor;

    /**
     * Route {@link PartRelationshipUpdateEvent}s to processor.
     *
     * @param payload Payload from broker.
     */
    @KafkaHandler
    public void route(final @Valid PartRelationshipUpdateEvent payload) {
        log.info("PartRelationshipUpdateEvent event received.");
        updateProcessor.process(payload);
        log.info("Event processed.");
    }
}
