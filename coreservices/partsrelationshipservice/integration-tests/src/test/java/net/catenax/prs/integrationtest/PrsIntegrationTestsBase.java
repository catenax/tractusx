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
import org.apache.kafka.common.serialization.StringDeserializer;
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
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
     * Kafka test configuration is needed to use kafka test container within {@link net.catenax.prs.services.MessageConsumerService}
     */
    @TestConfiguration
    static class KafkaTestContainersConfiguration {
        @Bean
        ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        @Bean
        public ConsumerFactory<String, Object> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        }

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_AUTO_OFFSET_RESET_CONFIG);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
            return props;
        }
    }

}