package net.catenax.prs.e2etest;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;

public class E2ETestBase {

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


    @BeforeEach
    public void setUp() {
        // If no config specified, run the smoke test against the service deployed in dev001.
        RestAssured.baseURI = System.getProperty("baseURI") == null ?
                "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com" : System.getProperty("baseURI");
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
