package net.catenax.prs.smoketest;

import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import net.catenax.prs.testing.UpdateRequestMother;
import org.junit.jupiter.api.BeforeEach;

public class SmokeTestsBase {

    protected static final String PATH_BY_VIN = "/api/v0.1/vins/{vin}/partsTree";
    protected static final String PATH_BY_IDS = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    protected static final String PATH_UPDATE_RELATIONSHIPS = "/broker-proxy/v0.1/partRelationshipUpdateList";
    protected static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    protected static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    protected static final String SAMPLE_VIN = "YS3DD78N4X7055320";
    protected static final String VIN = "vin";
    protected static final String VIEW = "view";
    protected static final String PRS_API_LOCALHOST_URI = "http://localhost:8080";
    protected static final String BROKER_PROXY_LOCALHOST_URI = "http://localhost:8081";

    private String userName;
    private String password;
    protected String brokerProxyUri;
    protected String prsApiUri;

    protected UpdateRequestMother generate = new UpdateRequestMother();

    @BeforeEach
    public void setUp() {
        // If no config specified, run the smoke test against localhost.
        prsApiUri = System.getProperty("baseURI", PRS_API_LOCALHOST_URI);
        brokerProxyUri = System.getProperty("brokerProxyBaseURI", System.getProperty("baseURI", BROKER_PROXY_LOCALHOST_URI));
        userName = System.getProperty("userName");
        password = System.getProperty("password");
    }


    protected RequestSpecification getRequestSpecification() {
        var specificationBuilder = new RequestSpecBuilder();

        // Add basic auth if a userName and password have been specified.
        if (userName != null && password != null) {
            var auth = new BasicAuthScheme();
            auth.setUserName(userName);
            auth.setPassword(password);
            specificationBuilder.setAuth(auth).build();
        }

        return specificationBuilder.build();
    }
}
