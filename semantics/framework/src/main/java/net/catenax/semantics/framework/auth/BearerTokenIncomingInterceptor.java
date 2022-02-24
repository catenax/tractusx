/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Spring Web Interceptor to save the bearer token for routing to delegation
 */
@RequiredArgsConstructor
public class BearerTokenIncomingInterceptor implements AsyncHandlerInterceptor {

    private final BearerTokenWrapper tokenWrapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            String token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());
            tokenWrapper.setToken(token);
        }

        return true;
    }
}