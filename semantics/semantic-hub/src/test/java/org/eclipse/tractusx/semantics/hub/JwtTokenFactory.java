/********************************************************************************
 * Copyright (c) 2021-2022 Robert Bosch Manufacturing Solutions GmbH
 * Copyright (c) 2021-2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.semantics.hub;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.eclipse.tractusx.semantics.AuthorizationEvaluator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

public class JwtTokenFactory {

    private String publicClientId;

    public JwtTokenFactory(String publicClientId){
        this.publicClientId = publicClientId;
    }

    private RequestPostProcessor authenticationWithRoles(String ... roles){
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("resource_access", Map.of(publicClientId, Map.of("roles", toJsonArray(roles) )))
                .build();
        Collection<GrantedAuthority> authorities = Collections.emptyList();
        return authentication(new JwtAuthenticationToken(jwt, authorities));
    }

    private static JSONArray toJsonArray(String ... elements){
        JSONArray jsonArray = new JSONArray();
        for (String element : elements){
            jsonArray.appendElement(element);
        }
        return jsonArray;
    }



    public RequestPostProcessor allRoles(){
        return authenticationWithRoles(
                AuthorizationEvaluator.Roles.ROLE_VIEW_SEMANTIC_MODEL,
                AuthorizationEvaluator.Roles.ROLE_ADD_SEMANTIC_MODEL,
                AuthorizationEvaluator.Roles.ROLE_UPDATE_SEMANTIC_MODEL,
                AuthorizationEvaluator.Roles.ROLE_DELETE_SEMANTIC_MODEL
        );
    }

    public RequestPostProcessor readModel(){
        return authenticationWithRoles(AuthorizationEvaluator.Roles.ROLE_VIEW_SEMANTIC_MODEL);
    }

    public RequestPostProcessor addModel(){
        return authenticationWithRoles(AuthorizationEvaluator.Roles.ROLE_ADD_SEMANTIC_MODEL);
    }

    public RequestPostProcessor updateModel(){
        return authenticationWithRoles(AuthorizationEvaluator.Roles.ROLE_UPDATE_SEMANTIC_MODEL);
    }

    public RequestPostProcessor deleteModel(){
        return authenticationWithRoles(AuthorizationEvaluator.Roles.ROLE_DELETE_SEMANTIC_MODEL);
    }

    public RequestPostProcessor withoutResourceAccess(){
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .build();
        Collection<GrantedAuthority> authorities = Collections.emptyList();
        return authentication(new JwtAuthenticationToken(jwt, authorities));
    }

    public RequestPostProcessor withoutRoles(){
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("resource_access", Map.of(publicClientId, new HashMap<String, String>()))
                .build();
        Collection<GrantedAuthority> authorities = Collections.emptyList();
        return authentication(new JwtAuthenticationToken(jwt, authorities));
    }

}
