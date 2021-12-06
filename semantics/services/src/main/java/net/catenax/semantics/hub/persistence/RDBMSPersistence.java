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

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.vavr.control.Try;
import net.catenax.semantics.hub.model.Model;
import net.catenax.semantics.hub.model.ModelList;
import net.catenax.semantics.hub.model.NewModel;
import net.catenax.semantics.hub.persistence.mapper.SemanticModelMapper;
import net.catenax.semantics.hub.persistence.model.ModelEntity;


@Component
public class RDBMSPersistence implements PersistenceLayer {
    @Autowired
    SemanticModelMapper mapper;

    @Autowired
    ModelRepository mr;

    @Override
    public ModelList getModels(@Nullable Boolean isPrivate, String namespaceFilter, String nameFilter, @Nullable String nameType, @Nullable String type, @Nullable String status, int page, int pageSize) {
        Pageable pageOptions = PageRequest.of(page, pageSize);

        Page<ModelEntity> result = null;

        // default name type is NAME (owl:Schema or bamm-c:Aspect)
        if(nameType==null) {
            nameType="_NAME_";
        }
                
        if("_NAME_".equals(nameType)) {
            result=mr.filterModels(isPrivate, namespaceFilter,  nameFilter, null, type, status, pageOptions);
        } else if("_DESCRIPTION_".equals(nameType)) {
            // TODO we should work with regular expressions here. problem is different regexp syntax for h2 and pgsql
            String contentFilter="%bamm:description \"%"+nameFilter+"%\"%";
            System.out.println("Got content filter "+contentFilter);
            result=mr.filterModels(isPrivate, namespaceFilter,  null, contentFilter, type, status, pageOptions);
        }else {
            // TODO we should work with regular expressions here. problem is different regexp syntax for h2 and pgsql
            String contentFilter="%"+nameFilter+"% a "+nameType+"%";
            result=mr.filterModels(isPrivate, namespaceFilter, null, contentFilter, type, status, pageOptions);            
        }

        List<Model> modelList = mapper.modelEntityListToModelDtoList(result.toList());

        ModelList modelListObject = new ModelList();
        modelListObject.setItems(modelList);
        modelListObject.setCurrentPage(result.getNumber());
        modelListObject.setItemCount(result.getNumberOfElements());
        modelListObject.setTotalItems((int) result.getTotalElements());
        modelListObject.setTotalPages(result.getTotalPages());

        return modelListObject;
    }

    @Override
    public Model getModel(String modelId) {
        Optional<ModelEntity> result = mr.findById(modelId);

        if(!result.isPresent()) {
            return null;
        }

        Model model = mapper.modelEntityToModelDto(result.get());
        return model;
    }

    @Override
    public Optional<String> getModelDefinition(String modelId) {
        Optional<ModelEntity> result = mr.findById(modelId);

        if(!result.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(result.get().getModelDefinition());
    }

    @Override 
    public Optional<Model> insertNewModel(NewModel newModel, String id, String version, String name) {
        ModelEntity modelEntity = mapper.newModelToModelEntity(newModel);
        modelEntity.setId(id);
        modelEntity.setName(name);
        modelEntity.setVersion(version);

        if(!mr.existsById(id)) {
            mr.save(modelEntity);
            mr.flush();
            return Optional.of(mapper.modelEntityToModelDto(modelEntity));
        }
        
        return Optional.empty();
    }

    @Override
    public Try<Void> deleteModel(String modelId) {
        Try<Void> deletionResult = Try.run(() -> mr.deleteById(modelId));

        mr.flush();

        return deletionResult;
    }

    @Override
    public Optional<Model> updateExistingModel(NewModel model, String id, String version, String name) {
        ModelEntity modelEntity = mapper.newModelToModelEntity(model);
        modelEntity.setId(id);
        modelEntity.setName(name);
        modelEntity.setVersion(version);

        if(mr.existsById(id)) {
            mr.save(modelEntity);
            mr.flush();
            return Optional.of(mapper.modelEntityToModelDto(modelEntity));
        }
        
        return Optional.empty();
    }
}
