//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerproxy.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * BrokerProxy configuration settings. Automatically populated by Spring from application.yml
 * and other configuration sources.
 */
@Component
@ConfigurationProperties(prefix = "brokerproxy")
@Data
public class BrokerProxyConfiguration {
    /**
     * The Base URL at which the API is externally accessible. Used in generated OpenAPI definition.
     */
    private URL apiUrl;

    /**
     * Kafka topic for parts relationships.
     */
    @Value("${brokerproxy.kafkaTopic.relationships}")
    private String kafkaTopicRelationships;

    /**
     * Kafka topic for parts aspects.
     */
    @Value("${brokerproxy.kafkaTopic.aspects}")
    private String kafkaTopicAspects;

    /**
     * Kafka topic for parts attributes.
     */
    @Value("${brokerproxy.kafkaTopic.attributes}")
    private String kafkaTopicAttributes;
}
