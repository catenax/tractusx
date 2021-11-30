import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * This class is responsible for running a performance test on connectors integrated with PRS.
 */
public class PrsConnectorPerformanceTest extends Simulation {

    private static final String connectorUri = System.getenv().getOrDefault("ConnectorURI", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/prs-connector-consumer/api/v0.1");
    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";
    private static final String ASPECT_MATERIAL = "MATERIAL";
    private static final String VIEW = "AS_BUILT";
    private static final int DEPTH = 2;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HttpProtocolBuilder httpProtocol = http.baseUrl(connectorUri)
            .acceptHeader("*/*").contentTypeHeader("application/json");


    // Trigger a get parts tree request. Then call status endpoint every second till it returns 200.
    private ScenarioBuilder scenarioBuilder = scenario("Trigger Get parts tree for a part.")
            // TODO: Decide right configurations (how many repeat, and how many users at once)
            .repeat(1)
            .on(exec(
                    http("Trigger partsTree request")
                            .post("/file")
                            .body(StringBody(getSerializedPartsTreeRequest()))
                            .check(status().is(200)).check(bodyString().saveAs("requestId"))
            )
                    // Call status endpoint every second, till it gives a 200 status code.
                    .exec(session -> session.set("status", -1))
                    .group("waitForCompletion").on(
                            doWhileDuring(session -> session.getInt("status") != 200, Duration.ofSeconds(120))
                                    .on(exec(http("Get status")
                                            .get(session -> String.format("/datarequest/%s/state", session.getString("requestId")))
                                            .check(status().saveAs("status")))
                                            .pause(Duration.ofSeconds(1)))));

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(10))).protocols(httpProtocol);
    }

    private static String getSerializedPartsTreeRequest() {
        Map<String, Object> params = new HashMap<>();
        params.put("partsTreeRequest", PartsTreeByObjectIdRequest.builder()
                .oneIDManufacturer(VEHICLE_ONEID)
                .objectIDManufacturer(VEHICLE_OBJECTID)
                .view(VIEW)
                .aspect(ASPECT_MATERIAL)
                .depth(DEPTH)
                .build());

        try {
            return MAPPER.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception serializing parts tree request", e);
        }
    }
}
