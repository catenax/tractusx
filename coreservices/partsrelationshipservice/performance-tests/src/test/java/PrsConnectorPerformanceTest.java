import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import net.catenax.prs.requests.PartsTreeByObjectIdRequest;

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
    // TODO: connectorAddress Should be removed after PR #307 gets merged.
    private static final String providerAddress = System.getenv().getOrDefault("ProviderAddress", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/bmw/mtpdc/connector");
    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";
    private static final String ASPECT_MATERIAL = "MATERIAL";
    private static final String VIEW = "AS_BUILT";
    private static final int DEPTH = 2;
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

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
                    .doWhile(session -> session.getInt("status") != 200)
                    .on(exec(http("Get status")
                            .get(session -> String.format("/datarequest/%s/state", session.getString("requestId")))
                            .check(status().saveAs("status")))
                            .pause(Duration.ofSeconds(1))));

    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }

    private static String getSerializedPartsTreeRequest() {
        Map<String, Object> params = new HashMap<>();
        // TODO: connectorAddress Should be removed after PR #307 gets merged.
        params.put("connectorAddress", providerAddress);
        params.put("partsTreeRequest", PartsTreeByObjectIdRequest.builder()
                .oneIDManufacturer(VEHICLE_ONEID)
                .objectIDManufacturer(VEHICLE_OBJECTID)
                .view(VIEW)
                .aspect(ASPECT_MATERIAL)
                .depth(DEPTH)
                .build());

        try {
            String bodyContent = objectMapper.writeValueAsString(params);
            System.out.println(bodyContent);
            return bodyContent;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing parts tree request", e);
        }
    }
}
