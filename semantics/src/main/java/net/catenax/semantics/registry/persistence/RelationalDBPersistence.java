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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.catenax.semantics.registry.model.DigitalTwin;
import net.catenax.semantics.registry.model.DigitalTwinCreate;
import net.catenax.semantics.registry.persistence.mapper.TwinAspectMapper;
import net.catenax.semantics.registry.persistence.model.TwinEntity;

@Component
public class RelationalDBPersistence implements PersistenceLayer {
    @Autowired
    TwinAspectMapper mapper;

    @Autowired
    TwinRepository twinRepository;

    @Override
    public List<DigitalTwin> getTwins(Boolean isPrivate, String namespaceFilter, String nameFilter, String type, int page,
            int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DigitalTwin getTwin(String modelId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DigitalTwin insertTwin(DigitalTwinCreate twin) {
        TwinEntity twinEntity = mapper.twinCreateDtoToTwinEntity(twin);

        twinRepository.save(twinEntity);

        twinRepository.flush();

        DigitalTwin resultTwin = mapper.twinEntityToTwinDto(twinEntity);

        return resultTwin;
    }
    
}
