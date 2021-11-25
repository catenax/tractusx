import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PrsPerformanceTest extends Simulation {

    // TODO: Use working ids

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/bmw/mtpdc/prs")
            .inferHtmlResources(AllowList(), DenyList())
            .acceptHeader("*/*")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader("PostmanRuntime/7.28.4");

    private Map<CharSequence, String> headers_0 = Map.of("Postman-Token", "408a553b-a4b0-4763-8bac-a389234d839c");


    private ScenarioBuilder scn = scenario("BasicSimulationJava")
            .repeat(100)
            .on(exec(
                    http("request_0")
                            .get("/api/v0.1/parts/cupidatat%20in%20ex/cupidatat%20in%20ex/partsTree?view=AS_BUILT&aspect=CE&depth=16618698")
                            .headers(headers_0)
                            // TODO: Use right assertion
                            .check(status().is(400))
            ));
    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
