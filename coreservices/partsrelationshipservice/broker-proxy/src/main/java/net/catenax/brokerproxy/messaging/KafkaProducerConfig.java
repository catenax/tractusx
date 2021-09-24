//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerproxy.messaging;

import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.annotations.ExcludeFromCodeCoverageGeneratedReport;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

/**
 * Factory for Kafka client templates.
 */
@Configuration
@Slf4j
@ExcludeFromCodeCoverageGeneratedReport
public class KafkaProducerConfig {

    /**
     * Create a {@link KafkaOperations} object allowing to send messages to the Kafka broker.
     *
     * @param settings client settings.
     * @return never returns {@literal null}.
     */
    @Bean
    @SuppressWarnings("PMD.UseConcurrentHashMap")
    public KafkaOperations<String, Object> kafkaTemplate(final KafkaSettings settings) {
        final var props = new HashMap<>(settings.getProperties());

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}
