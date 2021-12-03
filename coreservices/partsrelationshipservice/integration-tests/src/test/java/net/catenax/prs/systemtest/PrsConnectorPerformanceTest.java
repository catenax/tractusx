package net.catenax.prs.systemtest;

import io.gatling.javaapi.core.CoreDsl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * This class is responsible for running a performance test on connectors integrated with PRS.
 */
@Tag("SystemTests")
public class PrsConnectorPerformanceTest extends SystemTestsBase {

    @Test
    public void test() {
        runGatling(PerformanceTestsRunner.class);
    }

    public static class PerformanceTestsRunner extends Runner {
        {
            setUp(scenarioBuilder.injectOpen(CoreDsl.atOnceUsers(10))).protocols(httpProtocol);
        }
    }

}