//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.BackOff;

/**
 * Configuration for spring-kafka.
 * <p>
 * Dead-letter records are sent to a topic named {originalTopic}.DLT (the original topic name
 * suffixed with .DLT) and to the same partition as the original record. Therefore, the dead-letter
 * topic must have at least as many partitions as the original topic. If the message cannot be sent
 * to the dead-letter topic, it is not committed, and will therefore be retried. In case JSON
 * deserialization fails, the message is sent base64-encoded to the dead-letter topic.
 */
@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaConfiguration {

    /**
     * PRS configuration settings.
     */
    private final PrsConfiguration prsConfiguration;

    /**
     * Constructs a {@see ConcurrentKafkaListenerContainerFactory} to process
     * Kafka messages with exponential back-off retrying, and dead-lettering.
     *
     * @param configurer           listener factory configurer.
     * @param kafkaConsumerFactory consumer factory.
     * @param template             the {@link KafkaOperations} for publishing dead-letter messaging.
     * @return Guaranteed to never return {@literal null}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            final ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            final ConsumerFactory<Object, Object> kafkaConsumerFactory,
            final KafkaTemplate<Object, Object> template) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(
                new DeadLetterPublishingRecoverer(template), backOff()));
        return factory;
    }

    private BackOff backOff() {
        final var config = prsConfiguration.getProcessingRetry();
        final var backOff = new ExponentialBackOffWithMaxRetries(config.getMaxRetries());
        backOff.setInitialInterval(config.getInitialIntervalMilliseconds());
        backOff.setMultiplier(config.getMultiplier());
        backOff.setMaxInterval(config.getMaxIntervalMilliseconds());
        return backOff;
    }
}
