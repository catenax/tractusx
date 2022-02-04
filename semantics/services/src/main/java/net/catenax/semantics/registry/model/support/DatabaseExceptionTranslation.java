/*
 * Copyright (c) 2022 Robert Bosch Manufacturing Solutions GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.catenax.semantics.registry.model.support;

import org.springframework.dao.DuplicateKeyException;

public class DatabaseExceptionTranslation {

    private static final String DEFAULT_DUPLICATE_KEY_MESSAGE = "An entity for the given id does already exist.";

    public static String translate(DuplicateKeyException exception){
        String message = exception.getMessage();
        if(message == null ){
            return DEFAULT_DUPLICATE_KEY_MESSAGE;
        }

        if(message.toUpperCase().contains("SUBMODEL_SHELL_AK_01")){
            return "A SubmodelDescriptor with the given identification does already exists for this AssetAdministrationShell.";
        }

        if(message.toUpperCase().contains("SHELL_AK_01")){
            return "An AssetAdministrationShell for the given identification does already exists.";
        }

        if(message.toUpperCase().contains("SHELL_IDENTIFIER_AK_01")){
            return "A specificAssetId for the given key does already exist.";
        }



        return DEFAULT_DUPLICATE_KEY_MESSAGE;
    }
}
