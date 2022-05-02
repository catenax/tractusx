/********************************************************************************
 * Copyright (c) 2021-2022 Robert Bosch Manufacturing Solutions GmbH
 *
 * See the AUTHORS file(s) distributed with this work for additional
 * information regarding authorship.
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
package io.catenax.semantics.registry.mapper;

import io.catenax.semantics.aas.registry.model.Endpoint;
import io.catenax.semantics.aas.registry.model.Reference;
import io.catenax.semantics.aas.registry.model.SubmodelDescriptor;
import io.catenax.semantics.registry.model.Submodel;
import io.catenax.semantics.registry.model.SubmodelEndpoint;
import io.catenax.semantics.aas.registry.model.*;
import io.catenax.semantics.registry.model.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubmodelMapper {
    @Mappings({
            @Mapping(target="idExternal", source="identification"),
            @Mapping(target="descriptions", source="description"),
            @Mapping(target="semanticId", source = "semanticId")
    })
    Submodel fromApiDto(SubmodelDescriptor apiDto);

    @Mappings({
            @Mapping(target="interfaceName", source = "interface"),
            @Mapping(target="endpointAddress", source = "protocolInformation.endpointAddress"),
            @Mapping(target="endpointProtocol", source = "protocolInformation.endpointProtocol"),
            @Mapping(target="endpointProtocolVersion", source = "protocolInformation.endpointProtocolVersion"),
            @Mapping(target="subProtocol", source = "protocolInformation.subprotocol"),
            @Mapping(target="subProtocolBody", source = "protocolInformation.subprotocolBody"),
            @Mapping(target="subProtocolBodyEncoding", source = "protocolInformation.subprotocolBodyEncoding"),
    })
    SubmodelEndpoint fromApiDto(Endpoint apiDto);

    @InheritInverseConfiguration
    List<SubmodelDescriptor> toApiDto(Set<Submodel> shell);

    @InheritInverseConfiguration
    SubmodelDescriptor toApiDto(Submodel shell);

    @InheritInverseConfiguration
    Endpoint toApiDto(SubmodelEndpoint apiDto);

    default String map(Reference reference){
        return reference != null && reference.getValue() != null && !reference.getValue().isEmpty() ? reference.getValue().get(0) : null;
    }

    default Reference map(String semanticId){
        if(semanticId == null ||  semanticId.isBlank()) {
            return null;
        }
        Reference reference = new Reference();
        reference.setValue(List.of(semanticId));
        return reference;
    }

}
