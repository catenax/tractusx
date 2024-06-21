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
package org.eclipse.tractusx.semantics.registry.service;

import com.google.common.collect.ImmutableSet;
import org.eclipse.tractusx.semantics.registry.model.projection.ShellMinimal;
import org.eclipse.tractusx.semantics.registry.model.projection.SubmodelMinimal;
import org.eclipse.tractusx.semantics.registry.model.support.DatabaseExceptionTranslation;
import org.eclipse.tractusx.semantics.registry.repository.ShellIdentifierRepository;
import org.eclipse.tractusx.semantics.registry.repository.ShellRepository;
import org.eclipse.tractusx.semantics.registry.repository.SubmodelRepository;
import org.eclipse.tractusx.semantics.registry.dto.BatchResultDto;
import org.eclipse.tractusx.semantics.registry.dto.ShellCollectionDto;
import org.eclipse.tractusx.semantics.registry.model.Shell;
import org.eclipse.tractusx.semantics.registry.model.ShellIdentifier;
import org.eclipse.tractusx.semantics.registry.model.Submodel;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShellService {

    private final ShellRepository shellRepository;
    private final ShellIdentifierRepository shellIdentifierRepository;
    private final SubmodelRepository submodelRepository;

    public ShellService(ShellRepository shellRepository, ShellIdentifierRepository shellIdentifierRepository,
                        SubmodelRepository submodelRepository) {
        this.shellRepository = shellRepository;
        this.shellIdentifierRepository = shellIdentifierRepository;
        this.submodelRepository = submodelRepository;
    }

    @Transactional
    public Shell save(Shell shell) {
        return shellRepository.save(shell);
    }

    @Transactional(readOnly = true)
    public Shell findShellByExternalId(String externalShellId){
        return shellRepository.findByIdExternal(externalShellId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Shell for identifier %s not found", externalShellId)));
    }

    @Transactional(readOnly = true)
    public ShellCollectionDto findAllShells(int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "createdDate");
        Page<Shell> shellsPage = shellRepository.findAll(pageable);
        return ShellCollectionDto.builder()
                .currentPage(pageable.getPageNumber())
                .totalItems((int)shellsPage.getTotalElements())
                .totalPages(shellsPage.getTotalPages())
                .itemCount(shellsPage.getNumberOfElements())
                .items(shellsPage.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    public List<String> findExternalShellIdsByIdentifiersByExactMatch(Set<ShellIdentifier> shellIdentifiers){
        List<String[]> keyValueCombinations = shellIdentifiers.stream().map(shellIdentifier -> new String[]{shellIdentifier.getKey(), shellIdentifier.getValue()}).collect(Collectors.toList());
        return shellRepository.findExternalShellIdsByIdentifiersByExactMatch(keyValueCombinations, keyValueCombinations.size());
    }

    @Transactional(readOnly = true)
    public List<String> findExternalShellIdsByIdentifiersByAnyMatch(Set<ShellIdentifier> shellIdentifiers){
        List<String[]> keyValueCombinations = shellIdentifiers.stream().map(shellIdentifier -> new String[]{shellIdentifier.getKey(), shellIdentifier.getValue()}).collect(Collectors.toList());
        return shellRepository.findExternalShellIdsByIdentifiersByAnyMatch(keyValueCombinations);
    }

    @Transactional(readOnly = true)
    public List<Shell> findShellsByExternalShellIds(Set<String> externalShellIds){
        return shellRepository.findShellsByIdExternalIsIn(externalShellIds);
    }

    @Transactional
    public Shell update(String externalShellId, Shell shell){
        ShellMinimal shellFromDb = findShellMinimalByExternalId(externalShellId);
        return shellRepository.save(
                shell.withId(shellFromDb.getId()).withCreatedDate(shellFromDb.getCreatedDate())
        );
    }

    @Transactional
    public void deleteShell(String externalShellId) {
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        shellRepository.deleteById(shellId.getId());
    }

    @Transactional(readOnly = true)
    public Set<ShellIdentifier> findShellIdentifiersByExternalShellId(String externalShellId){
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        return shellIdentifierRepository.findByShellId(shellId.getId());
    }

    @Transactional
    public void deleteAllIdentifiers(String externalShellId){
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        shellIdentifierRepository.deleteShellIdentifiersByShellId(shellId.getId(), ShellIdentifier.GLOBAL_ASSET_ID_KEY);
    }

    @Transactional
    public Set<ShellIdentifier> save(String externalShellId, Set<ShellIdentifier> shellIdentifiers){
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        shellIdentifierRepository.deleteShellIdentifiersByShellId(shellId.getId(), ShellIdentifier.GLOBAL_ASSET_ID_KEY);

        List<ShellIdentifier> identifiersToUpdate = shellIdentifiers.stream().map(identifier -> identifier.withShellId(shellId.getId()))
                .collect(Collectors.toList());
        return ImmutableSet.copyOf(shellIdentifierRepository.saveAll(identifiersToUpdate));
    }

    @Transactional
    public Submodel save(String externalShellId, Submodel submodel){
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        return submodelRepository.save(submodel.withShellId(shellId.getId()));
    }

    @Transactional
    public Submodel update(String externalShellId, String externalSubmodelId, Submodel submodel){
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        SubmodelMinimal subModelId = findSubmodelMinimalByExternalId(shellId.getId(), externalSubmodelId);
        return submodelRepository.save(submodel
                .withId(subModelId.getId())
                .withShellId(shellId.getId())
        );
    }

    @Transactional
    public void deleteSubmodel(String externalShellId, String externalSubModelId) {
        ShellMinimal shellId = findShellMinimalByExternalId(externalShellId);
        SubmodelMinimal submodelId = findSubmodelMinimalByExternalId(shellId.getId(), externalSubModelId);
        submodelRepository.deleteById(submodelId.getId());
    }

    @Transactional(readOnly = true)
    public Submodel findSubmodelByExternalId(String externalShellId, String externalSubModelId){
        ShellMinimal shellIdByExternalId = findShellMinimalByExternalId(externalShellId);
        return submodelRepository
                .findByShellIdAndIdExternal(shellIdByExternalId.getId(), externalSubModelId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Submodel for identifier %s not found.", externalSubModelId)));
    }

    private SubmodelMinimal findSubmodelMinimalByExternalId(UUID shellId, String externalSubModelId ){
        return submodelRepository
                .findMinimalRepresentationByShellIdAndIdExternal(shellId, externalSubModelId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Submodel for identifier %s not found.", externalSubModelId)));
    }

    private ShellMinimal findShellMinimalByExternalId(String externalShellId){
        return shellRepository.findMinimalRepresentationByIdExternal(externalShellId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Shell for identifier %s not found", externalShellId)));
    }

    /**
     * Saves the provided shells. The transaction is scoped per shell. If saving of one shell fails others may succeed.
     * @param shells the shells to save
     * @return the result of each save operation
     */
    public List<BatchResultDto> saveBatch(List<Shell> shells) {
        return shells.stream().map(shell -> {
            try {
                shellRepository.save(shell);
                return new BatchResultDto("AssetAdministrationShell successfully created.",
                        shell.getIdExternal(), HttpStatus.OK.value());
            } catch (Exception e){
                if(e.getCause() instanceof DuplicateKeyException){
                    DuplicateKeyException duplicateKeyException = (DuplicateKeyException) e.getCause();
                    return new BatchResultDto( DatabaseExceptionTranslation.translate(duplicateKeyException),
                            shell.getIdExternal(),
                            HttpStatus.BAD_REQUEST.value());
                }
                return new BatchResultDto(String.format("Failed to create AssetAdministrationShell %s",
                        e.getMessage()), shell.getIdExternal(), HttpStatus.BAD_REQUEST.value());
            }
        }).collect(Collectors.toList());
    }

}
