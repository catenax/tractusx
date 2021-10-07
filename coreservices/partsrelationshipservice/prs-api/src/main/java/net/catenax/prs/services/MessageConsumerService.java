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

import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.stereotype.Service;

/**
 * Kafka message consumer service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.CommentSize"})
public class MessageConsumerService {

    /**
     * Service for processing parts tree update events.
     */
    private final PartsTreeUpdateProcessorService updateProcessorService;

    /**
     * Kafka consumer for {@link PartRelationshipUpdateEvent} messages.
     * @param event Parts relationship update event from broker.
     */
    @KafkaListener(topics = "${prs.kafkaTopics.relationships}")
    public void consume(final PartRelationshipUpdateEvent event) {
        log.info("PartRelationshipUpdateEvent event received.");
        updateProcessorService.update(event);
    }

    /**
     * Kafka consumer for {@link PartAttributeUpdateEvent} messages.
     * @param event Parts attribute update event from broker.
     */
    @KafkaListener(topics = "${prs.kafkaTopics.attributes}")
    public void consume(final PartAttributeUpdateEvent event) {
        log.info("PartAttributeUpdateEvent event received.");
        updateProcessorService.update(event);
    }

    /**
     * Kafka consumer for {@link PartAspectUpdateEvent} messages.
     * @param event Parts aspect update event from broker.
     */
    @KafkaListener(topics = "${prs.kafkaTopics.aspects}")
    public void consume(final PartAspectUpdateEvent event) {
        log.info("PartAspectUpdateEvent event received.");
        updateProcessorService.update(event);
    }

    /**
     *  By configuring the LoggingErrorHandler, we can log the content of the kafka message which app failed to deserialized (poison pill).
     * @return see {@link LoggingErrorHandler}
     */
    @Bean
    public LoggingErrorHandler errorHandler() {
        return new LoggingErrorHandler();
    }

}
