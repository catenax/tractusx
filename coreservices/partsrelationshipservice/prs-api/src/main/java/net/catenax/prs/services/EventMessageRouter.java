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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.prs.dtos.events.PartRelationshipsUpdateRequest;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Instant;

/**
 * Kafka message consumer service, routing event messages by payload type.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${prs.kafkaTopic}")
public class EventMessageRouter {

    /**
     * Service for processing {@link PartRelationshipsUpdateRequest}s.
     */
    private final PartRelationshipUpdateProcessor updateProcessor;

    /**
     * Route {@link PartRelationshipsUpdateRequest}s to processor.
     *
     * @param payload Payload from broker.
     * @param timestamp Timestamp of the record.
     */
    @KafkaHandler
    public void route(final @Payload @Valid PartRelationshipsUpdateRequest payload, final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        log.info("PartRelationshipUpdateRequest event received.");
        updateProcessor.process(payload, Instant.ofEpochMilli(timestamp));
        log.info("Event processed.");
    }
}
