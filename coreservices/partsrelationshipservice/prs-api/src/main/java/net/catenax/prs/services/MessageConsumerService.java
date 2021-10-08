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
import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * Kafka message consumer service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings({"PMD.CommentSize", "PMD.AvoidCatchingGenericException"})
//TODO: Include it in code coverage.
@ExcludeFromCodeCoverageGeneratedReport
public class MessageConsumerService {

    /**
     * NACK sleep time for a kafka consumer record negative acknowledgment.
     */
    private static final int NACK_SLEEP_IN_MS = 10;
    /**
     * Service for processing parts tree update events.
     */
    private final PartsTreeUpdateProcessorService updateProcessorService;

    /**
     * Kafka consumer for {@link PartRelationshipUpdateEvent} messages.
     * NOTE: Keeping concurrency = "1" to avoid any race-condition while processing data.
     * This is done here to keep it simple for Speedboat scope point of view.
     *
     * @param event Parts relationship update event from broker.
     * @param ack   Handle for acknowledging the processing of a ConsumerRecord.
     */
    @KafkaListener(topics = "${prs.kafkaTopics.relationships}", concurrency = "1")
    public void consume(final PartRelationshipUpdateEvent event, final Acknowledgment ack) {
        try {
            log.info("PartRelationshipUpdateEvent event received.");
            updateProcessorService.update(event);
            ack.acknowledge();
            log.info("PartRelationshipUpdateEvent event processed.");
        } catch (Exception exception) {
            log.error("PartRelationshipUpdateEvent Exception", exception);
            ack.nack(NACK_SLEEP_IN_MS);
        }
    }

    /**
     * Kafka consumer for {@link PartAttributeUpdateEvent} messages.
     * NOTE: Keeping concurrency = "1" to avoid any race-condition while processing data.
     * This is done here to keep it simple for Speedboat scope point of view.
     *
     * @param event Parts attribute update event from broker.
     * @param ack   Handle for acknowledging the processing of a ConsumerRecord.
     */
    @KafkaListener(topics = "${prs.kafkaTopics.attributes}", concurrency = "1")
    public void consume(final PartAttributeUpdateEvent event, final Acknowledgment ack) {
        try {
            log.info("PartAttributeUpdateEvent event received.");
            updateProcessorService.update(event);
            ack.acknowledge();
            log.info("PartAttributeUpdateEvent event processed.");
        } catch (Exception exception) {
            log.error("PartAttributeUpdateEvent Exception", exception);
            ack.nack(NACK_SLEEP_IN_MS);
        }
    }

    /**
     * Kafka consumer for {@link PartAspectUpdateEvent} messages.
     * NOTE: Keeping concurrency = "1" to avoid any race-condition while processing data.
     * This is done here to keep it simple for Speedboat scope point of view.
     *
     * @param event Parts aspect update event from broker.
     * @param ack   Handle for acknowledging the processing of a ConsumerRecord.
     */
    @KafkaListener(topics = "${prs.kafkaTopics.aspects}", concurrency = "1")
    public void consume(final PartAspectUpdateEvent event, final Acknowledgment ack) {
        try {
            log.info("PartAspectUpdateEvent event received.");
            updateProcessorService.update(event);
            ack.acknowledge();
            log.info("PartAspectUpdateEvent event processed.");
        } catch (Exception exception) {
            log.error("PartAspectUpdateEvent Exception", exception);
            ack.nack(NACK_SLEEP_IN_MS);
        }
    }

    /**
     * By configuring the LoggingErrorHandler, we can log the content of the kafka message which app failed to deserialized (poison pill).
     *
     * @return see {@link LoggingErrorHandler}
     */
    @Bean
    public LoggingErrorHandler errorHandler() {
        return new LoggingErrorHandler();
    }

}
