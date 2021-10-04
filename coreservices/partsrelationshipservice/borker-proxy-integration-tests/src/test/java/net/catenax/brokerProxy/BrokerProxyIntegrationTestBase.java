//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerProxy;

import io.restassured.RestAssured;
import net.catenax.brokerproxy.BrokerProxyApplication;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Tag("IntegrationTests")
@Import(BrokerProxyIntegrationTestBase.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = {BrokerProxyApplication.class}, webEnvironment = RANDOM_PORT)
public class BrokerProxyIntegrationTestBase {

    private static final String KAFKA_TEST_CONTAINER_IMAGE = "confluentinc/cp-kafka:5.4.3";

    protected final BrokerProxyMother brokerProxyMother = new BrokerProxyMother();

    public static KafkaContainer kafka;

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void initKafkaTestContainer() {
        kafka = new KafkaContainer(DockerImageName.parse(KAFKA_TEST_CONTAINER_IMAGE));
        kafka.start();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterAll
    public static void stopKafkaTestContainer() {
        kafka.stop();
    }

    @TestConfiguration
    static class KafkaTestContainersConfiguration {

        @Bean
        public ProducerFactory<String, Object> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, Object> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }


    }
}
