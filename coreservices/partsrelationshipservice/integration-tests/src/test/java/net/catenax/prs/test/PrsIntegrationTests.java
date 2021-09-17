package net.catenax.prs.test;

import io.micrometer.core.instrument.util.IOUtils;
import io.restassured.RestAssured;
import net.catenax.prs.PrsApplication;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = {PrsApplication.class}, webEnvironment = RANDOM_PORT)
public class PrsIntegrationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUpClass(){
        RestAssured.port = port;
    }

    @Test
    public void greetingShouldReturnDefaultMessage() {
        get("/api/v0.1/vins/BMWOVCDI21L5DYEUU/partsTree?view=AS_MAINTAINED").then().assertThat().body(equalTo(
        IOUtils.toString( getClass().getClassLoader().getResourceAsStream("response_1631610272167.json"))));
    }
}