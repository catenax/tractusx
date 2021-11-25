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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import net.catenax.semantics.registry.model.DigitalTwin;
import net.catenax.semantics.registry.model.DigitalTwinCollection;
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
    public DigitalTwinCollection getTwins(String key, String value, int pageSize, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<TwinEntity> result = twinRepository.searchDigitalTwinsByLocalIdentifier(key, value, pageable);

        List<DigitalTwin> twinList = mapper.digitalTwinEntityListToDigitalTwinDtoList(result.toList());

        DigitalTwinCollection collection = new DigitalTwinCollection()
            .items(twinList)
            .currentPage(pageable.getPageNumber())
            .totalItems((int) result.getTotalElements())
            .totalPages(result.getTotalPages())
            .itemCount(result.getNumberOfElements());
        
        return collection;
    }

    @Override
    public DigitalTwin getTwin(String twinId) {
        Optional<TwinEntity> resultEntity = twinRepository.findById(twinId);

        if(resultEntity.isEmpty()) {
            return null;
        }

        return mapper.twinEntityToTwinDto(resultEntity.get());
    }

    @Override
    public List<DigitalTwin> insertTwinList(List<DigitalTwinCreate> digitalTwinCreateList) {
        List<TwinEntity> twinEntityList = mapper.digitalTwinCreateDtoListToTwinEntityList(digitalTwinCreateList);

        List<TwinEntity> twinEntityListResult = twinRepository.saveAll(twinEntityList);
        twinRepository.flush();

        return mapper.digitalTwinEntityListToDigitalTwinDtoList(twinEntityListResult);
    }

    @Override
    public DigitalTwin insertTwin(DigitalTwinCreate twin) {
        TwinEntity twinEntity = mapper.twinCreateDtoToTwinEntity(twin);

        TwinEntity twinEntityResult = twinRepository.save(twinEntity);

        twinRepository.flush();

        DigitalTwin resultTwin = mapper.twinEntityToTwinDto(twinEntityResult);

        return resultTwin;
    }
    
    @Override
    public boolean deleteTwin(String twinId) {
        if(!twinRepository.existsById(twinId)) {
            return false;
        }
        
        twinRepository.deleteById(twinId);
        twinRepository.flush();

        return true;
    }
}
