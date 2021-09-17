package net.catenax.prs.test;

import io.micrometer.core.instrument.util.IOUtils;
import net.catenax.prs.PrsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = {PrsApplication.class}, webEnvironment = RANDOM_PORT)
public class PrsIntegrationTests {

    @Test
    public void greetingShouldReturnDefaultMessage() {
        get("/api/v0.1/vins/BMWOVCDI21L5DYEUU/partsTree").then().assertThat().body(equalTo(
        IOUtils.toString( getClass().getClassLoader().getResourceAsStream("response_1631610272167.json"))));
    }
}