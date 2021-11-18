//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.monitor;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging monitor using java.util.logging.
 */
public class LoggerMonitor implements Monitor {

    /**
     * Global logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LoggerMonitor.class.getName());

    @Override
    public void severe(final Supplier<String> supplier, final Throwable... errors) {
        log(supplier, Level.SEVERE, errors);
    }

    @Override
    public void severe(final String message, final Throwable... errors) {
        severe(() -> message, errors);
    }

    @Override
    public void severe(final Map<String, Object> data) {
        data.forEach((key, value) -> LOGGER.log(Level.SEVERE, key, value));
    }

    @Override
    public void warning(final Supplier<String> supplier, final Throwable... errors) {
        log(supplier, Level.WARNING, errors);
    }

    @Override
    public void warning(final String message, final Throwable... errors) {
        warning(() -> message, errors);
    }

    @Override
    public void info(final Supplier<String> supplier, final Throwable... errors) {
        log(supplier, Level.INFO, errors);
    }

    @Override
    public void info(final String message, final Throwable... errors) {
        log(() -> message, Level.INFO, errors);
    }

    @Override
    public void debug(final Supplier<String> supplier, final Throwable... errors) {
        log(supplier, Level.FINE, errors);
    }

    @Override
    public void debug(final String message, final Throwable... errors) {
        debug(() -> message, errors);
    }

    private void log(final Supplier<String> supplier, final Level level, final Throwable... errors) {
        if (errors.length == 0) {
            LOGGER.log(level, supplier);
        } else {
            Arrays.stream(errors).forEach(error -> LOGGER.log(level, supplier.get(), error));
        }
    }

}
