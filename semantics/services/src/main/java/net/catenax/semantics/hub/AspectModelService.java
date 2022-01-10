/*
 * Copyright (c) 2022 Bosch Software Innovations GmbH. All rights reserved.
 */

package net.catenax.semantics.hub;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

import io.openmanufacturing.sds.aspectmodel.resolver.services.VersionedModel;
import io.openmanufacturing.sds.aspectmodel.validation.report.ValidationReport;
import io.openmanufacturing.sds.metamodel.Aspect;
import io.vavr.control.Try;
import net.catenax.semantics.hub.api.ModelsApiDelegate;
import net.catenax.semantics.hub.bamm.BammHelper;
import net.catenax.semantics.hub.model.NewSemanticModel;
import net.catenax.semantics.hub.model.SemanticModel;
import net.catenax.semantics.hub.model.SemanticModelList;
import net.catenax.semantics.hub.persistence.PersistenceLayer;

public class AspectModelService implements ModelsApiDelegate {

   private final PersistenceLayer persistenceLayer;
   private final BammHelper bammHelper;

   public AspectModelService( final PersistenceLayer persistenceLayer, BammHelper bammHelper ) {
      this.persistenceLayer = persistenceLayer;
      this.bammHelper = bammHelper;
   }

   @Override
   public ResponseEntity<SemanticModelList> getModelList( Integer pageSize,
         Integer page,
         String namespaceFilter,
         String nameFilter,
         String nameType,
         String status ) {

      try {
         String decodedType = null;
         if ( nameType != null ) {
            decodedType = URLDecoder.decode( nameType, StandardCharsets.UTF_8.name() );
         }
         final String decodedNamespace = URLDecoder.decode( namespaceFilter,
               StandardCharsets.UTF_8.name() );
         final String decodedName = java.net.URLDecoder.decode( nameFilter,
               StandardCharsets.UTF_8.name() );

         final SemanticModelList list = persistenceLayer.getModels( decodedNamespace, decodedName, decodedType,
               status, page,
               pageSize );

         return new ResponseEntity( list, HttpStatus.OK );
      } catch ( final java.io.UnsupportedEncodingException uee ) {
         return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
      }
   }

   @Override
   public ResponseEntity<SemanticModel> getModelByUrn( final String urn ) {
      final SemanticModel model = persistenceLayer.getModel( urn );

      if ( model == null ) {
         return new ResponseEntity<>( HttpStatus.NOT_FOUND );
      }

      return new ResponseEntity<>( model, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<SemanticModel> createModelWithUrn( final NewSemanticModel newModel ) {

      final Optional<SemanticModel> resultingModel = persistenceLayer.insertNewModel( newModel );

      if ( !resultingModel.isPresent() ) {
         return new ResponseEntity( "Model ID already exists!", HttpStatus.BAD_REQUEST );
      }

      return new ResponseEntity<>( resultingModel.get(), HttpStatus.OK );
   }

   @Override
   public ResponseEntity<org.springframework.core.io.Resource> getModelDiagram( final String modelId ) {
      final Optional<String> modelDefinition = persistenceLayer.getModelDefinition( modelId );

      if ( !modelDefinition.isPresent() ) {
         return new ResponseEntity( HttpStatus.NOT_FOUND );
      }

      final Try<VersionedModel> versionedModel = bammHelper.loadBammModel( modelDefinition.get() );

      if ( !versionedModel.isSuccess() ) {
         return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final byte[] pngBytes = bammHelper.generatePng( versionedModel.get() );

      if ( pngBytes == null ) {
         return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final HttpHeaders headers = new HttpHeaders();

      return new ResponseEntity( pngBytes, headers, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelJsonSchema( final String modelId ) {
      final Optional<String> modelDefinition = persistenceLayer.getModelDefinition( modelId );

      if ( modelDefinition.isEmpty() ) {
         return new ResponseEntity( HttpStatus.NOT_FOUND );
      }

      final Try<VersionedModel> versionedModel = bammHelper.loadBammModel( modelDefinition.get() );

      if ( versionedModel.isFailure() ) {
         return new ResponseEntity( versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final Try<Aspect> aspect = bammHelper.getAspectFromVersionedModel( versionedModel.get() );

      if ( aspect.isFailure() ) {
         return new ResponseEntity( aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST );
      }

      final Aspect bammAspect = aspect.get();

      final JsonNode json = bammHelper.getJsonSchema( bammAspect );

      return new ResponseEntity( json, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelDocu( final String modelId ) {
      final Optional<String> modelDefinition = persistenceLayer.getModelDefinition( modelId );

      if ( !modelDefinition.isPresent() ) {
         return new ResponseEntity( HttpStatus.NOT_FOUND );
      }

      final Try<VersionedModel> versionedModel = bammHelper.loadBammModel( modelDefinition.get() );

      if ( versionedModel.isFailure() ) {
         return new ResponseEntity( versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final Try<byte[]> docuResult = bammHelper.getHtmlDocu( versionedModel.get() );
      if ( docuResult.isFailure() ) {
         return new ResponseEntity( docuResult.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType( MediaType.TEXT_HTML );

      return new ResponseEntity( docuResult.get(), headers, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelFile( final String modelId ) {
      final Optional<String> modelDefinition = persistenceLayer.getModelDefinition( modelId );

      if ( !modelDefinition.isPresent() ) {
         return new ResponseEntity( HttpStatus.NOT_FOUND );
      }

      return new ResponseEntity( modelDefinition.get(), HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> deleteModel( final String modelId ) {
      final Try<Void> result = persistenceLayer.deleteModel( modelId );

      if ( result.isFailure() ) {
         if ( result.getCause() instanceof EmptyResultDataAccessException ) {
            return new ResponseEntity( "Model ID does not exist!", HttpStatus.BAD_REQUEST );
         }

         return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
      }

      return new ResponseEntity( HttpStatus.NO_CONTENT );
   }

   @Override
   public ResponseEntity<SemanticModel> modifyModel( final NewSemanticModel newModel ) {
      final Try<VersionedModel> model = bammHelper.loadBammModel( newModel.getModel() );

      if ( model.isFailure() ) {
         return new ResponseEntity( model.getCause().getMessage(), HttpStatus.BAD_REQUEST );
      }

      final ValidationReport validation = bammHelper.validateModel( model );

      if ( !validation.conforms() ) {
         return new ResponseEntity( validation.getValidationErrors().toString(), HttpStatus.BAD_REQUEST );
      }

      final Try<Aspect> aspect = bammHelper.getAspectFromVersionedModel( model.get() );

      if ( aspect.isFailure() ) {
         return new ResponseEntity( aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST );
      }

      final Aspect bammAspect = aspect.get();

      final Optional<SemanticModel> resultingModel = persistenceLayer.updateExistingModel( newModel,
            bammAspect.getAspectModelUrn().get().toString(), bammAspect.getAspectModelUrn().get().getVersion(),
            bammAspect.getName() );

      if ( resultingModel.isPresent() ) {
         return new ResponseEntity<>( resultingModel.get(), HttpStatus.OK );
      }

      return new ResponseEntity( "Model does not exist!", HttpStatus.BAD_REQUEST );
   }

   @Override
   public ResponseEntity<Void> getModelOpenApi( final String modelId, final String baseUrl ) {
      final Optional<String> modelDefinition = persistenceLayer.getModelDefinition( modelId );

      if ( modelDefinition.isEmpty() ) {
         return new ResponseEntity( HttpStatus.NOT_FOUND );
      }

      final Try<VersionedModel> versionedModel = bammHelper.loadBammModel( modelDefinition.get() );

      if ( versionedModel.isFailure() ) {
         return new ResponseEntity( versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final Try<Aspect> aspect = bammHelper.getAspectFromVersionedModel( versionedModel.get() );

      if ( aspect.isFailure() ) {
         return new ResponseEntity( aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST );
      }

      final Aspect bammAspect = aspect.get();

      final String openApiJson = bammHelper.getOpenApiDefinitionJson( bammAspect, baseUrl );

      return new ResponseEntity( openApiJson, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelExamplePayloadJson( final String modelId ) {
      final Optional<String> modelDefinition = persistenceLayer.getModelDefinition( modelId );

      if ( modelDefinition.isEmpty() ) {
         return new ResponseEntity( HttpStatus.NOT_FOUND );
      }

      final Try<VersionedModel> versionedModel = bammHelper.loadBammModel( modelDefinition.get() );

      if ( versionedModel.isFailure() ) {
         return new ResponseEntity( versionedModel.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
      }

      final Try<Aspect> aspect = bammHelper.getAspectFromVersionedModel( versionedModel.get() );

      if ( aspect.isFailure() ) {
         return new ResponseEntity( aspect.getCause().getMessage(), HttpStatus.BAD_REQUEST );
      }

      final Aspect bammAspect = aspect.get();

      final Try<String> result = bammHelper.getExamplePayloadJson( bammAspect );

      if ( result.isFailure() ) {
         return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
      }

      return new ResponseEntity( result.get(), HttpStatus.OK );
   }
}
