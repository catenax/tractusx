/*
 * Copyright (c) 2021 Robert Bosch Manufacturing Solutions GmbH
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

package net.catenax.semantics.registry.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.catenax.semantics.registry.model.Aspect;
import net.catenax.semantics.registry.model.DigitalTwin;
import net.catenax.semantics.registry.model.DigitalTwinCreate;
import net.catenax.semantics.registry.model.HttpEndpoint;
import net.catenax.semantics.registry.model.LocalIdentifier;
import net.catenax.semantics.registry.model.ModelReference;
import net.catenax.semantics.registry.persistence.model.AspectEntity;
import net.catenax.semantics.registry.persistence.model.HttpEndpointEntity;
import net.catenax.semantics.registry.persistence.model.LocalIdentifierEntity;
import net.catenax.semantics.registry.persistence.model.TwinEntity;

@Mapper(componentModel = "spring")
public interface TwinAspectMapper {
    @Mapping(source = "description", target = "description")
    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "localIdentifiers", target = "localIdentifiers")
    @Mapping(source = "aspects", target = "aspects")
    public TwinEntity twinCreateDtoToTwinEntity(DigitalTwinCreate twin);

    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    public LocalIdentifierEntity localIdentifierCreateDtoTLocalIdentifierEntity(LocalIdentifier localIdentifier);

    @Mapping(source = "modelReference", target = "modelReference")
    @Mapping(source = "httpEndpoints", target = "httpEndpoints")
    public AspectEntity aspectCreateDtoToAspectEntity(Aspect aspect);

    @Mapping(source = "url", target = "url")
    @Mapping(source = "method", target = "method")
    public HttpEndpointEntity httpEndpointToHttpEndpointEntity(HttpEndpoint httpEndpoint);

    public default String modelReferenceDtoToString(ModelReference modelReference) {
        return modelReference.getUrn();
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "localIdentifiers", target = "localIdentifiers")
    @Mapping(source = "aspects", target = "aspects")
    public DigitalTwin twinEntityToTwinDto(TwinEntity twin);

    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    public LocalIdentifier localIdentifierEntityToLocalIdentifierDto(LocalIdentifierEntity localIdentifier);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "modelReference", target = "modelReference")
    @Mapping(source = "httpEndpoints", target = "httpEndpoints")
    public Aspect aspectEntityToAspectDto(AspectEntity aspect);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "method", target = "method")
    public HttpEndpoint httpEndpointEntityToHttpEndpointDto(HttpEndpointEntity httpEndpoint);

    public default ModelReference modelReferenceDtoToString(String urnString) {
        ModelReference modelReference = new ModelReference();

        modelReference.setUrn(urnString);

        return modelReference;
    }

    public List<DigitalTwin> digitalTwinEntityListToDigitalTwinDtoList(List<TwinEntity> twinEntitiesList);

    public List<TwinEntity> digitalTwinCreateDtoListToTwinEntityList(List<DigitalTwinCreate> twinCreateList);
}
