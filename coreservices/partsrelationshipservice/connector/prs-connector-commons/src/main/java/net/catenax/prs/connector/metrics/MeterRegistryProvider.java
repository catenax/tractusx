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
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;

/**
 * Provider for Micrometer meter registry.
 */
@ExcludeFromCodeCoverageGeneratedReport
public final class MeterRegistryProvider {

    private MeterRegistryProvider() {
    }

    /**
     * Provides Micrometer Jmx meter registry.
     * @return see {@link JmxMeterRegistry}
     */
    public static JmxMeterRegistry jmxMeterRegistry() {
        final JmxMeterRegistry jmxMeterRegistry = new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM);
        jmxMeterRegistry.start();
        return jmxMeterRegistry;
    }
}
