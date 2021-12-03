package net.catenax.prs.systemtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.connector.requests.PartsTreeRequest;
import net.catenax.prs.dtos.PartsTreeView;

import java.time.Duration;

public class Runner extends Simulation {

    private static final String connectorUri = System.getenv()
            .getOrDefault("ConnectorURI", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/prs-connector-consumer/api/v0.1");
    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";
    private static final int DEPTH = 2;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String vehicleOneId;
    private String vehicleObjectId;
    private int depth;

    public Runner(int depth, String vehicleObjectId, String vehicleOneId) {
        this.depth = depth;
        this.vehicleObjectId = vehicleObjectId;
        this.vehicleOneId = vehicleOneId;
    }

    public Runner() {
        this.vehicleObjectId = VEHICLE_OBJECTID;
        this.vehicleOneId = VEHICLE_ONEID;
        this.depth = DEPTH;
    }

    protected HttpProtocolBuilder httpProtocol = HttpDsl.http.baseUrl(connectorUri)
            .acceptHeader("*/*").contentTypeHeader("application/json");
    // Trigger a get parts tree request. Then call status endpoint every second till it returns 200.

    protected ScenarioBuilder scenarioBuilder = CoreDsl.scenario("Trigger Get parts tree for a part.")
            // TODO: Decide right configurations (how many repeat, and how many users at once)
            .repeat(1)
            .on(CoreDsl.exec(
                            HttpDsl.http("Trigger partsTree request")
                                    .post("/retrievePartsTree")
                                    .body(CoreDsl.StringBody(getSerializedPartsTreeRequest()))
                                    .check(HttpDsl.status().is(200)).check(CoreDsl.bodyString().saveAs("requestId"))
                    )
                    // Call status endpoint every second, till it gives a 200 status code.
                    .exec(session -> session.set("status", -1))
                    .group("waitForCompletion").on(
                            CoreDsl.doWhileDuring(session -> session.getInt("status") != 200, Duration.ofSeconds(12))
                                    .on(CoreDsl.exec(HttpDsl.http("Get status")
                                                    .get(session -> String.format("/datarequest/%s/state", session.getString("requestId")))
                                                    .check(HttpDsl.status().saveAs("status")))
                                            .pause(Duration.ofSeconds(1)))));

    private String getSerializedPartsTreeRequest() {
        var params = PartsTreeRequest.builder()
                .byObjectIdRequest(
                        PartsTreeByObjectIdRequest.builder()
                                .oneIDManufacturer(vehicleOneId)
                                .objectIDManufacturer(vehicleObjectId)
                                .view(PartsTreeView.AS_BUILT.name())
                                .aspect(PrsConnectorStressTest.ASPECT_MATERIAL)
                                .depth(depth)
                                .build()).build();

        try {
            return MAPPER.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Exception serializing parts tree request", e);
        }
    }
}