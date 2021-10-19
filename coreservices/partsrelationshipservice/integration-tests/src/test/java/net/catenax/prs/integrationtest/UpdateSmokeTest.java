package net.catenax.prs.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.catenax.prs.PrsApplication;
import net.catenax.prs.dtos.*;
import net.catenax.prs.dtos.events.PartRelationshipUpdate;
import net.catenax.prs.dtos.events.PartRelationshipsUpdateRequest;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static net.catenax.prs.dtos.PartsTreeView.AS_MAINTAINED;
import static net.catenax.prs.testing.TestUtil.DATABASE_TESTCONTAINER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


public class UpdateSmokeTest extends PrsIntegrationTestsBase {

    protected static final String PATH_BY_VIN = "/api/v0.1/vins/{vin}/partsTree";
    protected static final String PATH_BY_IDS = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    protected static final String PATH_UPDATE_ATTRIBUTES = "/broker-proxy/v0.1/partRelationshipUpdateList";

    protected static final String SAMPLE_VIN = "YS3DD78N4X7055320";
    protected static final String VIN = "vin";
    protected static final String VIEW = "view";
    protected static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    protected static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    protected String userName;
    protected String password;

    private Faker faker = new Faker();


    ObjectMapper om = new ObjectMapper();

    @ClassRule
    public static GenericContainer brokerProxyServer
            = new GenericContainer("partsrelationshipservice_broker-proxy")
            .withExposedPorts(4005, 8081)
            .withEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS", "host.docker.internal:" + kafka.getMappedPort(9093));

    private String brokerProxyUrl;

    @BeforeEach
    public void setUp() {
        brokerProxyServer.start();
        brokerProxyUrl = "http://"
                + brokerProxyServer.getContainerIpAddress()
                + ":" + brokerProxyServer.getMappedPort(8081);
        om.registerModule(new JavaTimeModule());

    }

    @Test
    public void updateRelationshipsAndGetPartsTree_success() throws JsonProcessingException {

        //RequestSpecification specification = getRequestSpecification();

        PartId child = PartId.builder()
                .withOneIDManufacturer(faker.company().name())
                .withObjectIDManufacturer(faker.lorem().characters(10, 20))
                .build();
        PartId parent = PartId.builder()
                .withOneIDManufacturer(faker.company().name())
                .withObjectIDManufacturer(faker.lorem().characters(10, 20))
                .build();
        PartRelationship partRelationship = PartRelationship.builder()
                .withParent(parent)
                .withChild(child)
                .build();
        var updateRequest = PartRelationshipsUpdateRequest.builder()
                .withRelationships(List.of(
                        PartRelationshipUpdate.builder()
                                .withEffectTime(Instant.now())
                                .withRemove(false)
                                .withStage(PartLifecycleStage.BUILD)
                                .withRelationship(partRelationship)
                                .build()))
                .build();

        var json = om.writeValueAsString(updateRequest);

        given()
                .basePath(brokerProxyUrl)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .post(PATH_UPDATE_ATTRIBUTES)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());


    }


}
