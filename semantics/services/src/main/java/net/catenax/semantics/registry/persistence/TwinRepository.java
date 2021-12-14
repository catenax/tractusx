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

package net.catenax.semantics.registry.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.catenax.semantics.registry.persistence.model.TwinEntity;

@Repository
public interface TwinRepository extends JpaRepository<TwinEntity, String> {
    @Query(value = "SELECT DISTINCT t FROM TwinEntity t INNER JOIN t.localIdentifiers l WHERE (l.key = :key OR :key is null) AND (l.value = :value OR :value is null)")
    public Page<TwinEntity> searchDigitalTwinsByLocalIdentifier(@Param("key") String key, @Param("value") String value, Pageable pageable);

    @Query(value = "SELECT DISTINCT t FROM TwinEntity t INNER JOIN t.localIdentifiers l WHERE CONCAT(l.key, ':', l.value) IN :keyValueCombinations")
    public List<TwinEntity> fetchDigitalTwins(@Param("keyValueCombinations") List<String> keyValueCombinations);
}
