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
package net.catenax.semantics.registry.repository;

import net.catenax.semantics.registry.model.Shell;
import net.catenax.semantics.registry.model.projection.ShellMinimal;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ShellRepository extends PagingAndSortingRepository<Shell, UUID> {
    Optional<Shell> findByIdExternal(String idExternal);

    @Query("select s.id, s.created_date from shell s where s.id_external = :idExternal")
    Optional<ShellMinimal> findMinimalRepresentationByIdExternal(String idExternal);

    @Query("select distinct s.id_external from shell s where s.id in (select distinct si.fk_shell_id from shell_identifier si where CONCAT(si.key, ':', si.value) in (:keyValueCombinations))")
    List<String> findExternalShellIdsByIdentifiers(@Param("keyValueCombinations") Set<String> keyValueCombinations);
}
