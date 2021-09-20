package net.catenax.prs.test;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipWithInfos;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import net.catenax.prs.PrsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.get;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = {PrsApplication.class}, webEnvironment = RANDOM_PORT)
public class PrsIntegrationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUpClass(){
        RestAssured.port = port;
    }

    @Test
    public void getPartsTreeByVin() throws Exception {
        // Arrange
        var objectMapper = new ObjectMapper();
        var expected = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("response_1631610272167.json"), PartRelationshipWithInfos.class);

        // Act
        var response = get("/api/v0.1/vins/BMWOVCDI21L5DYEUU/partsTree?view=AS_MAINTAINED")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();

        // Assert
        assertThatJson(response)
                .isEqualTo(json(expected));
    }
}