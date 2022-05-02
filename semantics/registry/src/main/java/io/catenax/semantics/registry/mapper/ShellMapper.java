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

import io.catenax.semantics.aas.registry.model.AssetAdministrationShellDescriptor;
import io.catenax.semantics.aas.registry.model.AssetAdministrationShellDescriptorCollection;
import io.catenax.semantics.aas.registry.model.BatchResult;
import io.catenax.semantics.aas.registry.model.IdentifierKeyValuePair;
import io.catenax.semantics.registry.dto.BatchResultDto;
import io.catenax.semantics.registry.dto.ShellCollectionDto;
import io.catenax.semantics.registry.model.Shell;
import io.catenax.semantics.registry.model.ShellIdentifier;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(uses = {SubmodelMapper.class}, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ShellMapper {
    @Mappings({
            @Mapping(target = "idExternal", source = "identification"),
            @Mapping(target = "identifiers", source = "specificAssetIds"),
            @Mapping(target = "descriptions", source = "description"),
            @Mapping(target = "submodels", source = "submodelDescriptors"),
    })
    Shell fromApiDto(AssetAdministrationShellDescriptor apiDto);

    List<Shell> fromListApiDto(List<AssetAdministrationShellDescriptor> apiDto);

    ShellIdentifier fromApiDto(IdentifierKeyValuePair apiDto);

    Set<ShellIdentifier> fromApiDto(List<IdentifierKeyValuePair> apiDto);

    AssetAdministrationShellDescriptorCollection toApiDto(ShellCollectionDto shell);

    @Mappings({
            @Mapping(target = "identification", source = "idExternal"),
    })
    BatchResult toApiDto(BatchResultDto batchResult);

    List<BatchResult> toListApiDto(List<BatchResultDto> batchResults);

    @InheritInverseConfiguration
    AssetAdministrationShellDescriptor toApiDto(Shell shell);

    List<AssetAdministrationShellDescriptor> toApiDto(List<Shell> shell);

    List<IdentifierKeyValuePair> toApiDto(Set<ShellIdentifier> shell);

    @AfterMapping
    default Shell convertGlobalAssetIdToShellIdentifier(AssetAdministrationShellDescriptor apiDto, @MappingTarget Shell shell){
        return ShellMapperCustomization.globalAssetIdToShellIdentifier(apiDto, shell);
    }

    @AfterMapping
    default void convertShellIdentifierToGlobalAssetId(Shell shell, @MappingTarget AssetAdministrationShellDescriptor apiDto){
        ShellMapperCustomization.shellIdentifierToGlobalAssetId(shell, apiDto);
    }

    @AfterMapping
    default void removeGlobalAssetIdFromIdentifiers(@MappingTarget List<IdentifierKeyValuePair> apiDto){
        ShellMapperCustomization.removeGlobalAssetIdIdentifier(apiDto);
    }

}
