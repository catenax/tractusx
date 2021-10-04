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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import net.catenax.brokerproxy.annotations.ExcludeFromCodeCoverageGeneratedReport;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Map;

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
     * Map of all Kafka topics.
     */
    private Map<String, String> kafkaTopics;

    /**
     * Gets kafka topic for parts relationships.
     * @return Parts relationship kafka topic name.
     */
    @SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "Map initialized via config.")
    @ExcludeFromCodeCoverageGeneratedReport
    public String getPartsRelationshipTopic() {
        return kafkaTopics.get("relationships");
    }

    /**
     * Gets kafka topic for parts aspects.
     * @return Parts aspects kafka topic name.
     */
    @SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "Map initialized via config.")
    @ExcludeFromCodeCoverageGeneratedReport
    public String getPartsAspectsTopic() {
        return kafkaTopics.get("aspects");
    }

    /**
     * Gets kafka topic for parts attributes.
     * @return Parts attributes kafka topic name.
     */
    @SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "Map initialized via config.")
    @ExcludeFromCodeCoverageGeneratedReport
    public String getPartsAttributesTopic() {
        return kafkaTopics.get("attributes");
    }
}

