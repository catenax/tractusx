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
package org.eclipse.tractusx.semantics;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Map;

import static org.eclipse.tractusx.semantics.AuthorizationEvaluator.Roles.*;

/**
 * This class contains methods validating JWT tokens for correctness and ensuring that the JWT token contains a desired role.
 * The methods are meant to be used in Spring Security expressions for RBAC on API operations.
 *
 * The Catena-X JWT Tokens are configured as in the example below:
 *
 *   resource_access:
 *      catenax-portal:
 *          roles:
 *              - add_digitial_twin
 *              - delete_digitial_twin
 *              - ... .. ..
 *
 * Before checking for an existing role, the token is validated first. If any attributes are not set as the expected structure,
 * the token will be considered invalid. Invalid tokens result in 403.
 *
 */
public class AuthorizationEvaluator {

    private final String clientId;

    public AuthorizationEvaluator(String clientId) {
        this.clientId = clientId;
    }

    public boolean hasRoleViewSemanticModel() {
        return containsRole(ROLE_VIEW_SEMANTIC_MODEL);
    }

    public boolean hasRoleAddSemanticModel() {
        return containsRole(ROLE_ADD_SEMANTIC_MODEL);
    }

    public boolean hasRoleUpdateSemanticModel() {
        return containsRole(ROLE_UPDATE_SEMANTIC_MODEL);
    }

    public boolean hasRoleDeleteSemanticModel() {
        return containsRole(ROLE_DELETE_SEMANTIC_MODEL);
    }


    private boolean containsRole(String role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof JwtAuthenticationToken)){
            return false;
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) (authentication);
        Map<String, Object> claims = jwtAuthenticationToken.getToken().getClaims();

        Object resourceAccess = claims.get("resource_access");
        if (!(resourceAccess instanceof Map)) {
            return false;
        }

        Object resource = ((Map<String, Object>) resourceAccess).get(clientId);
        if(!(resource instanceof Map)){
            return false;
        }

        Object roles =  ((Map<String, Object>)resource).get("roles");
        if(!(roles instanceof Collection)){
            return false;
        }

        Collection<String> rolesList = (Collection<String> ) roles;
        return rolesList.contains(role);
    }

    /**
     * Represents the roles defined for the registry.
     */
    public static final class Roles {
        public static final String ROLE_VIEW_SEMANTIC_MODEL = "view_semantic_model";
        public static final String ROLE_UPDATE_SEMANTIC_MODEL = "update_semantic_model";
        public static final String ROLE_ADD_SEMANTIC_MODEL = "add_semantic_model";
        public static final String ROLE_DELETE_SEMANTIC_MODEL = "delete_semantic_model";
    }

}

