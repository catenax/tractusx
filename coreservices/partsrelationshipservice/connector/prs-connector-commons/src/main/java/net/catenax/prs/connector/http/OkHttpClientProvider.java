//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.http;

import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.jmx.JmxMeterRegistry;
import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * EDC core OkHttpClient does not have an event listener.
 * This utility class provides OkHttpClient with metric event listener.
 */
@ExcludeFromCodeCoverageGeneratedReport
public final class OkHttpClientProvider {

    private static final int DEFAULT_TIMEOUT = 30;
    private static final String METRIC_NAME = "okhttp3.monitor";

    private OkHttpClientProvider() {
    }

    /**
     * Provide OkHttpClient with metric event listener.
     * @param meterRegistry Micrometer registry. See {@link JmxMeterRegistry}
     * @return see {@link OkHttpClient}.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static OkHttpClient httpClient(JmxMeterRegistry meterRegistry) {

        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .eventListener(OkHttpMetricsEventListener
                        .builder(meterRegistry, METRIC_NAME)
                        .build())
                .build();
    }


}
