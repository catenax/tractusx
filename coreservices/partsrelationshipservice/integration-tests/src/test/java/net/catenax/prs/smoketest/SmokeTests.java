//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.smoketest;

import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.catenax.prs.dtos.PartRelationshipsWithInfos;
import net.catenax.prs.dtos.events.PartRelationshipsUpdateRequest;
import net.catenax.prs.testing.UpdateRequestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.catenax.prs.dtos.PartsTreeView.AS_BUILT;
import static net.catenax.prs.dtos.PartsTreeView.AS_MAINTAINED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

/**
 * Smoke tests verify that the cloud infrastructure where PRS runs is working as expected
 * @see <a href="https://confluence.catena-x.net/display/CXM/PRS+Testing+Strategy">PRS Testing Strategy</a>
 */
@Tag("SmokeTests")
public class SmokeTests {

    protected static final String PATH_BY_VIN = "/api/v0.1/vins/{vin}/partsTree";
    protected static final String PATH_BY_IDS = "/api/v0.1/parts/{oneIDManufacturer}/{objectIDManufacturer}/partsTree";
    protected static final String PATH_UPDATE_RELATIONSHIPS = "/broker-proxy/v0.1/partRelationshipUpdateList";
    protected static final String ONE_ID_MANUFACTURER = "oneIDManufacturer";
    protected static final String OBJECT_ID_MANUFACTURER = "objectIDManufacturer";
    private static final String SAMPLE_VIN = "YS3DD78N4X7055320";
    private static final String VIN = "vin";
    private static final String VIEW = "view";
    public static final String PRS_API_LOCALHOST_URI = "http://localhost:8080";
    public static final String BROKER_PROXY_LOCALHOST_URI = "http://localhost:8081";

    private String userName;
    private String password;
    private String brokerProxyUri;
    private String prsApiUri;

    private UpdateRequestMother generate = new UpdateRequestMother();

    @BeforeEach
    public void setUp() {
        // If no config specified, run the smoke test against the service deployed in dev001.
        prsApiUri = getPropertyOrDefault("baseURI", PRS_API_LOCALHOST_URI);
        brokerProxyUri = getPropertyOrDefault("brokerProxyBaseURI", getPropertyOrDefault("baseURI", BROKER_PROXY_LOCALHOST_URI));
        userName = System.getProperty("userName");
        password = System.getProperty("password");
    }

    @Test
    public void getPartsTreeByVin_success() {

        given()
            .spec(getRequestSpecification())
            .baseUri(prsApiUri)
            .pathParam(VIN, SAMPLE_VIN)
            .queryParam(VIEW, AS_MAINTAINED)
        .when()
            .get(PATH_BY_VIN)
        .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .body("relationships", hasSize(greaterThan(0)))
            .body("partInfos", hasSize(greaterThan(0)));
    }

    @Test
    public void updateRelationshipsAndGetPartsTree_success() {

        RequestSpecification specification = getRequestSpecification();

        var partRelationshipUpdate = generate.partRelationshipUpdate();
        var updateRequest = PartRelationshipsUpdateRequest.builder().withRelationships(List.of(partRelationshipUpdate)).build();
        var partRelationship = partRelationshipUpdate.getRelationship();
        var parent = partRelationship.getParent();

        given()
            .spec(specification)
            .baseUri(brokerProxyUri)
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .post(PATH_UPDATE_RELATIONSHIPS)
        .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

        await().untilAsserted(() -> {
            var response =
                    given()
                        .spec(specification)
                        .baseUri(prsApiUri)
                        .pathParam(ONE_ID_MANUFACTURER, parent.getOneIDManufacturer())
                        .pathParam(OBJECT_ID_MANUFACTURER, parent.getObjectIDManufacturer())
                        .queryParam(VIEW, AS_BUILT)
                    .when()
                        .get(PATH_BY_IDS)
                    .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(PartRelationshipsWithInfos.class);

            assertThat(response.getRelationships()).containsExactly(partRelationship);
        });

    }

    private String getPropertyOrDefault(String baseURI, String prsApiLocalhostUri) {
        return System.getProperty(baseURI) == null ?
                prsApiLocalhostUri : System.getProperty(baseURI);
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
