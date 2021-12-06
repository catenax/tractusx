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

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.openmanufacturing.sds.aspectmodel.resolver.services.VersionedModel;
import io.openmanufacturing.sds.aspectmodel.validation.report.ValidationReport;
import io.openmanufacturing.sds.metamodel.Aspect;
import io.vavr.control.Try;
import net.catenax.semantics.hub.api.ModelsApiDelegate;
import net.catenax.semantics.hub.bamm.BammHelper;
import net.catenax.semantics.hub.model.Model;
import net.catenax.semantics.hub.model.ModelList;
import net.catenax.semantics.hub.model.NewModel;
import net.catenax.semantics.hub.persistence.PersistenceLayer;


@Service
public class ModelsService implements ModelsApiDelegate {
    @Autowired
    PersistenceLayer ps;

    @Autowired
    BammHelper bamm;

    @Override
    public ResponseEntity<ModelList> getModelList(Integer pageSize, Integer page, String namespaceFilter,
            String nameFilter, String nameType, Boolean isPrivate, String type, String status) {

        try {
            String decodedType=null;
            if(nameType!=null) {
                decodedType=java.net.URLDecoder.decode(nameType, java.nio.charset.StandardCharsets.UTF_8.name());
            }
            String decodedNamespace=java.net.URLDecoder.decode(namespaceFilter, java.nio.charset.StandardCharsets.UTF_8.name());
            String decodedName=java.net.URLDecoder.decode(nameFilter, java.nio.charset.StandardCharsets.UTF_8.name());
            
            ModelList list = ps.getModels(isPrivate, decodedNamespace, decodedName, decodedType, type, status, page, pageSize);

            return new ResponseEntity(list, HttpStatus.OK);
        } catch(java.io.UnsupportedEncodingException uee) {
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Model> getModelById(String modelId) {
        Model model = ps.getModel(modelId);

        if (model == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Model>(model, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Model> createModelWithId(NewModel newModel) {
        Try<VersionedModel> model = bamm.loadBammModel(newModel.getModel());

        if(model.isFailure()) {
            return new ResponseEntity(model.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        ValidationReport validation = bamm.validateModel(model);

        if(!validation.conforms()) {
            return new ResponseEntity(validation.getValidationErrors().toString(), HttpStatus.BAD_REQUEST);
        }

        Try<Aspect> aspect = bamm.getAspectFromVersionedModel(model.get());

        if(aspect.isFailure()) {
            return new ResponseEntity(aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        Aspect bammAspect = aspect.get();

        Optional<Model> resultingModel = ps.insertNewModel(newModel, bammAspect.getAspectModelUrn().get().toString(), bammAspect.getAspectModelUrn().get().getVersion(), bammAspect.getName());

        if(!resultingModel.isPresent()) {
            return new ResponseEntity("Model ID already exists!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(resultingModel.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getModelDiagram(String modelId) {
        Optional<String> modelDefinition = ps.getModelDefinition(modelId);

        if(!modelDefinition.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Try<VersionedModel> versionedModel = bamm.loadBammModel(modelDefinition.get());

        if(!versionedModel.isSuccess()) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        byte[] pngBytes = bamm.generatePng(versionedModel.get());
        
        if(pngBytes == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity(pngBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> getModelJsonSchema(String modelId) {
        Optional<String> modelDefinition = ps.getModelDefinition(modelId);

        if(modelDefinition.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Try<VersionedModel> versionedModel = bamm.loadBammModel(modelDefinition.get());

        if(versionedModel.isFailure()) {
            return new ResponseEntity(versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Try<Aspect> aspect = bamm.getAspectFromVersionedModel(versionedModel.get());

        if(aspect.isFailure()) {
            return new ResponseEntity(aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        Aspect bammAspect = aspect.get();

        JsonNode json = bamm.getJsonSchema(bammAspect);

        return new ResponseEntity(json, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> getModelDocu(String modelId) {
        Optional<String> modelDefinition = ps.getModelDefinition(modelId);

        if(!modelDefinition.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Try<VersionedModel> versionedModel = bamm.loadBammModel(modelDefinition.get());

        if(versionedModel.isFailure()) {
            return new ResponseEntity(versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Try<byte[]> docuResult = bamm.getHtmlDocu(versionedModel.get());
        if(docuResult.isFailure()) {
            return new ResponseEntity(docuResult.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity(docuResult.get(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> getModelFile(String modelId) {
        Optional<String> modelDefinition = ps.getModelDefinition(modelId);

        if(!modelDefinition.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(modelDefinition.get(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> deleteModel(String modelId) {
        Try<Void> result = ps.deleteModel(modelId);

        if(result.isFailure()) {
            if(result.getCause() instanceof EmptyResultDataAccessException) {
                return new ResponseEntity("Model ID does not exist!", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Model> modifyModel(NewModel newModel) {
        Try<VersionedModel> model = bamm.loadBammModel(newModel.getModel());

        if(model.isFailure()) {
            return new ResponseEntity(model.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        ValidationReport validation = bamm.validateModel(model);

        if(!validation.conforms()) {
            return new ResponseEntity(validation.getValidationErrors().toString(), HttpStatus.BAD_REQUEST);
        }

        Try<Aspect> aspect = bamm.getAspectFromVersionedModel(model.get());

        if(aspect.isFailure()) {
            return new ResponseEntity(aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        Aspect bammAspect = aspect.get();

        Optional<Model> resultingModel = ps.updateExistingModel(newModel, bammAspect.getAspectModelUrn().get().toString(), bammAspect.getAspectModelUrn().get().getVersion(), bammAspect.getName());

        if(resultingModel.isPresent()) {
            return new ResponseEntity<>(resultingModel.get(), HttpStatus.OK);
        }
        
        return new ResponseEntity("Model does not exist!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Void> getModelOpenApi(String modelId, String baseUrl) {
        Optional<String> modelDefinition = ps.getModelDefinition(modelId);

        if(modelDefinition.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Try<VersionedModel> versionedModel = bamm.loadBammModel(modelDefinition.get());

        if(versionedModel.isFailure()) {
            return new ResponseEntity(versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Try<Aspect> aspect = bamm.getAspectFromVersionedModel(versionedModel.get());

        if(aspect.isFailure()) {
            return new ResponseEntity(aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        Aspect bammAspect = aspect.get();

        String openApiJson = bamm.getOpenApiDefinitionJson(bammAspect, baseUrl);

        return new ResponseEntity(openApiJson, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> getModelExamplePayloadJson(String modelId) {
        Optional<String> modelDefinition = ps.getModelDefinition(modelId);

        if(modelDefinition.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Try<VersionedModel> versionedModel = bamm.loadBammModel(modelDefinition.get());

        if(versionedModel.isFailure()) {
            return new ResponseEntity(versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Try<Aspect> aspect = bamm.getAspectFromVersionedModel(versionedModel.get());

        if(aspect.isFailure()) {
            return new ResponseEntity(aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

        Aspect bammAspect = aspect.get();

        Try<String> result = bamm.getExamplePayloadJson(bammAspect);

        if(result.isFailure()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(result.get(), HttpStatus.OK);
    }
}
