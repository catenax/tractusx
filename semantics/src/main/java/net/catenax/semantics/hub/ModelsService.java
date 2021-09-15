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

package net.catenax.semantics.hub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.catenax.semantics.hub.api.ModelsApiDelegate;
import net.catenax.semantics.hub.model.Model;
import net.catenax.semantics.hub.persistence.PersistenceLayer;
import net.catenax.semantics.hub.persistence.mapper.SemanticModelMapper;

@Service
public class ModelsService implements ModelsApiDelegate {
    @Autowired
    PersistenceLayer ps;

    @Autowired
    SemanticModelMapper mapper;

    @Override
    public ResponseEntity<List<Model>> getModelList(Integer pageSize, Integer page, String namespaceFilter,
            String nameFilter, Boolean isPrivate, String type) {

        List<Model> list = mapper.modelEntityListToModelDtoList(
                ps.getModels(isPrivate, namespaceFilter, nameFilter, type, page, pageSize));

        return new ResponseEntity<List<Model>>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Model> getModelById(String modelId) {
        Model model = mapper.modelEntityToModelDto(ps.getModel(modelId));

        if (model == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Model>(model, HttpStatus.OK);
    }
}
