//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.metrics;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Provider for OkHttpClient with metric event listener.
 */
@ExcludeFromCodeCoverageGeneratedReport
public final class OkHttpClientProvider {

    private OkHttpClientProvider() {
    }

    /**
     * @return OkHttpClient with metric event listener.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
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
        final JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
        jmxMeterRegistry.start();
        return jmxMeterRegistry;
    }
}
