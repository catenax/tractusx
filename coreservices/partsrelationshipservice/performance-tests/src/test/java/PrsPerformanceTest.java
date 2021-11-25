import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PrsPerformanceTest extends Simulation {

    private static final String prsURI = System.getProperty("PrsURI", "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/bmw/mtpdc/prs");
    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";
    private static final String VIEW = "AS_BUILT";
    private static final int DEPTH = 2;

    private HttpProtocolBuilder httpProtocol = http.baseUrl(prsURI);

    final String pathParams = String.format("/api/v0.1/parts/%s/%s/partsTree?view=%s&aspect=CE&depth=%s", VEHICLE_ONEID, VEHICLE_OBJECTID, VIEW, DEPTH);
    private ScenarioBuilder scn = scenario("BasicSimulationJava")
            .repeat(100)
            .on(exec(
                    http("request_0")
                            .get(pathParams)
                            .check(status().is(200))
            ));

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
