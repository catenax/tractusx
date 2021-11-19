//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.middleware;

import net.catenax.prs.connector.monitor.LoggerMonitor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class HttpInterceptor implements Interceptor {

    private static final Logger LOGGER = Logger.getLogger(HttpInterceptor.class.getName());

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {

        Request request = chain.request();

        LOGGER.log(Level.FINE, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        
        return chain.proceed(request);

    }
}
