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

package net.catenax.semantics.hub.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.catenax.semantics.hub.persistence.model.ModelEntity;

/**
 * JPA compatible db interface
 */
@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, String> {
    public ModelEntity getModelById(String id);

    /**
     * search without contentType
     * @param isPrivate
     * @param nameFilter
     * @param namespaceFilter
     * @param contentFilter
     * @param type
     * @param pageable
     * @return resultset
     */
    @Query(value = "SELECT m FROM ModelEntity m WHERE (:isPrivate is null OR m._private = :isPrivate) AND m.name LIKE %:nameFilter% AND m.id LIKE %:namespaceFilter% AND (:contentFilter is null OR m.modelDefinition LIKE %:contentFilter%) AND (:type is null OR m.type = :type)")
    public Page<ModelEntity> filterModels(@Param("isPrivate") Boolean isPrivate, @Param("nameFilter") String nameFilter, @Param("namespaceFilter") String namespaceFilter, @Param("contentFilter") String contentFilter, @Param("type") String type, Pageable pageable);

}
