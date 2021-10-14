//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import net.catenax.prs.PrsApplication;
import net.catenax.prs.configuration.PrsConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.catenax.prs.testing.TestUtil.DATABASE_TESTCONTAINER;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Tag("IntegrationTests")
@TestPropertySource(properties = DATABASE_TESTCONTAINER)
@Import(PrsIntegrationTestsBase.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = {PrsApplication.class}, webEnvironment = RANDOM_PORT)
@DirtiesContext
public class PrsIntegrationTestsBase {

    protected static final String PATH = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    protected static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    protected static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    protected static final String VIEW = "view";

    private static final String KAFKA_TEST_CONTAINER_IMAGE = "confluentinc/cp-kafka:5.4.3";
    private static final String KAFKA_AUTO_OFFSET_RESET_CONFIG = "earliest";
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        /*
          jackson-datatype-jsr310 module is needed to support java.time.Instant.
         */
        objectMapper.registerModule(new JavaTimeModule());
    }

    private static KafkaContainer kafka;

    @LocalServerPort
    private int port;

    protected final PartsTreeApiResponseMother expected = new PartsTreeApiResponseMother();

    /**
     * PRS configuration settings.
     */
    @Autowired
    protected PrsConfiguration configuration;

    @Autowired
    private KafkaOperations<String, Object> kafkaOperations;

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

    /**
     * Publish update event to given kafka topic.
     * @param event Update event to be published.
     */
    protected void publishUpdateEvent(Object event) {
        var send = kafkaOperations.send(configuration.getKafkaTopic(), event);
        try {
            send.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Kafka test configuration is needed to use kafka test container
     */
    @TestConfiguration
    static class KafkaTestContainersConfiguration {
        @Bean
        ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
            return factory;
        }

        @Bean
        KafkaTemplate<String, Object> kafkaOperations() {
            return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs()));
        }

        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_AUTO_OFFSET_RESET_CONFIG);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
            return props;
        }

        public Map<String, Object> producerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            props.put(ProducerConfig.RETRIES_CONFIG, 3);
            return props;
        }
    }
}
