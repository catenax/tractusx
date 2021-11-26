import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PrsPerformanceTest extends Simulation {

    private static final String connectorUri = System.getenv().getOrDefault("ConnectorURI", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/prs-connector-consumer/api/v0.1");
    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";
    private static final String VIEW = "AS_BUILT";
    private static final int DEPTH = 2;

    private HttpProtocolBuilder httpProtocol = http.baseUrl(connectorUri)
            .acceptHeader("*/*").contentTypeHeader("application/json");
    private ScenarioBuilder scn = scenario("Get parts tree for a part.")
            .repeat(1)
            .on(exec(
                    http("Trigger partsTree request")
                            .post("/file")
                            .body(RawFileBody("performance-tests/src/test/java/body.json"))
                            .check(status().is(200)).check(bodyString().saveAs("requestId"))
                    )
                    // Call status endpoint every second, till it gives a 200 status code.
                    .exec(session -> session.set("status", -1))
                    .doWhile(session -> session.getInt("status") != 200)
                    .on(exec(http("Get status")
                            .get(session -> "/datarequest/" + session.getString("requestId") + "/state")
                            .check(status().saveAs("status")))
                            .pause(Duration.ofSeconds(1))));

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
