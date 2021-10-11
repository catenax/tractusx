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

import com.catenax.partsrelationshipservice.dtos.messaging.EventCategory;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAspectUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartAttributeUpdateEvent;
import com.catenax.partsrelationshipservice.dtos.messaging.PartRelationshipUpdateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
public class MessageConsumerService {

    /**
     * NACK sleep time for a kafka consumer record negative acknowledgment.
     */
    private static final int NACK_SLEEP_IN_MS = 10;

    /**
     * Jackson's object mapper. See {@link ObjectMapper}. Mapper instances are fully thread-safe.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Service for processing parts tree update events.
     */
    private final PartsTreeUpdateProcessorService updateProcessorService;

    static {
        /*
          jackson-datatype-jsr310 module is needed to support java.time.Instant.
         */
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * Kafka consumer for prs data update events.
     *
     * @param record PRS data update event from broker.
     * @param ack    Handle for acknowledging the processing of a ConsumerRecord.
     */
    @KafkaListener(topics = "${prs.kafkaTopic}")
    @SuppressFBWarnings(value = "CRLF_INJECTION_LOGS", justification = "Event update message is published via trusted source.")
    public void consume(final ConsumerRecord<String, String> record, final Acknowledgment ack) {
        try {
            final var eventCategory = EnumUtils.getEnum(EventCategory.class, record.key(), EventCategory.UNDEFINED);

            switch (eventCategory) {
                case PARTS_ASPECT:
                    log.info("PartAspectUpdateEvent event received.");
                    updateProcessorService.update(toEventObject(record.value(), PartAspectUpdateEvent.class));
                    break;
                case PARTS_ATTRIBUTE:
                    log.info("PartAttributeUpdateEvent event received.");
                    updateProcessorService.update(toEventObject(record.value(), PartAttributeUpdateEvent.class));
                    break;
                case PARTS_RELATIONSHIP:
                    log.info("PartRelationshipUpdateEvent event received.");
                    updateProcessorService.update(toEventObject(record.value(), PartRelationshipUpdateEvent.class));
                    break;
                default:
                    log.error("Unexpected event received. Key {} Value {}", record.key(), record.value());
                    break;
            }
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

    /**
     * For event json to event object mapping.
     *
     * @param eventJson Event json as received on broker topic.
     * @param clazz     Event object to be mapped upon.
     * @param <T>       Type of event object.
     * @return Mapped event object
     * @throws JsonProcessingException if json parser fails.
     */
    private <T> T toEventObject(final String eventJson, final Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(eventJson, clazz);
    }

}
