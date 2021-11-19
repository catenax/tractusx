package net.catenax.prs.connector.metrics;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

@ExcludeFromCodeCoverageGeneratedReport
public class OkHttpClientProvider {

    public static OkHttpClient httpClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .eventListener(OkHttpMetricsEventListener
                        .builder(jmxMeterRegistry(), "okhttp3.monitor")
                        .build())
                .build();
    }

    private static JmxMeterRegistry jmxMeterRegistry() {
        JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
        jmxMeterRegistry.start();
        return jmxMeterRegistry;
    }
}
