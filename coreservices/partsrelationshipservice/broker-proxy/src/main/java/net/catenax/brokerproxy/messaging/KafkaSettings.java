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

import lombok.Data;
import net.catenax.brokerproxy.annotations.ExcludeFromCodeCoverageGeneratedReport;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Kafka client configuration settings. Automatically populated by Spring from application.yml
 * and other configuration sources.
 */
@Component
@ConfigurationProperties(prefix = "kafka")
@Data
@ExcludeFromCodeCoverageGeneratedReport
public class KafkaSettings {
    /**
     * Kafka topic.
     */
    private String topic;
    /**
     * Kafka client settings, with underscores replacing periods to make it
     * easier to define configuration in YAML.
     */
    private Map<String, Object> properties;

    /**
     * Initialize Kafka client settings, replacing underscores with periods.
     */
    @PostConstruct
    public void init() {
        properties = properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().replace('_', '.'), e -> e.getValue()));
    }
}
