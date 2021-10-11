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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaHandler;
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
@KafkaListener(topics = "${prs.kafkaTopic}")
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
     * Kafka consumer for prs data update events.
     *
     * @param payload PRS data update event from broker.
     * @param ack     Handle for acknowledging the processing of a ConsumerRecord.
     */
    @SuppressFBWarnings(value = "CRLF_INJECTION_LOGS", justification = "Event update message is published via trusted source.")
    @KafkaHandler
    public void consumePartRelationshipUpdateEvent(final PartRelationshipUpdateEvent payload, final Acknowledgment ack) {
        try {
            log.info("PartRelationshipUpdateEvent event received.");
            updateProcessorService.update(payload);
            ack.acknowledge();
            log.info("Event processed.");
        } catch (Exception exception) {
            log.error("Exception occurred during prs update event processing", exception);
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
