package net.catenax.prs.test;

import com.catenax.partsrelationshipservice.dtos.PartRelationshipsWithInfos;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import net.catenax.prs.PrsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.catenax.partsrelationshipservice.dtos.PartsTreeView.AS_MAINTAINED;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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
        var expected = objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("response_1631610272167.json"), PartRelationshipsWithInfos.class);

        // Act
        var response =
            given()
                .pathParam("vin", "BMWOVCDI21L5DYEUU")
                .queryParam("view", AS_MAINTAINED)
            .when()
                .get("/api/v0.1/vins/{vin}/partsTree")
            .then()
                .assertThat()
                    .statusCode(HttpStatus.OK.value())
            .extract().as(PartRelationshipsWithInfos.class);

        // Assert
        assertThat(response).isEqualTo(expected);
    }
}