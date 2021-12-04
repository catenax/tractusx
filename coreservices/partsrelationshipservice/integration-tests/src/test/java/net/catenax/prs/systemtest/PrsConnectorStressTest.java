package net.catenax.prs.systemtest;

import io.gatling.javaapi.core.CoreDsl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * This class is responsible for running stress tests on connectors integrated with PRS.
 */
@Tag("StressTests")
public class PrsConnectorStressTest extends SystemTestsBase {

    @Test
    public void test() {
        runGatling(StressTestsRunner.class);
    }

    public static class StressTestsRunner extends Runner {

        {
            // generate an open workload injection profile
            // with levels of 10, 15, 20, 25 and 30 arriving users per second
            // separated by linear ramps lasting 10 seconds
            // each level lasting 10 seconds
            // triggers part tree request 1000 times during all levels and 800 during ramps
            setUp(scenarioBuilder.injectOpen(
                    CoreDsl.incrementUsersPerSec(5)
                            .times(5)
                            .eachLevelLasting(10)
                            .separatedByRampsLasting(10)
                            .startingFrom(10))).protocols(httpProtocol);
        }

        @Override
        protected String getSerializedPartsTreeRequest(int depth, String vehicleObjectId, String vehicleOneId) {
            return super.getSerializedPartsTreeRequest(5, "OXCNTJT4D6AWSGAK3", "CAXSWPFTJQEVZNZZ");
        }
    }
}