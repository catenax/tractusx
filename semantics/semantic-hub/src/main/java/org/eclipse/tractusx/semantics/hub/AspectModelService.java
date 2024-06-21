/*
 * Copyright (c) 2022 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.eclipse.tractusx.semantics.hub;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.eclipse.tractusx.semantics.hub.domain.ModelPackageStatus;
import org.eclipse.tractusx.semantics.hub.domain.ModelPackageUrn;
import org.eclipse.tractusx.semantics.hub.model.SemanticModel;
import org.eclipse.tractusx.semantics.hub.model.SemanticModelList;
import org.eclipse.tractusx.semantics.hub.model.SemanticModelStatus;
import org.eclipse.tractusx.semantics.hub.model.SemanticModelType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

import io.openmanufacturing.sds.aspectmodel.resolver.services.VersionedModel;
import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;
import io.openmanufacturing.sds.metamodel.Aspect;
import org.eclipse.tractusx.semantics.hub.bamm.BammHelper;
import org.eclipse.tractusx.semantics.hub.persistence.PersistenceLayer;
import io.vavr.control.Try;
import org.eclipse.tractusx.semantics.hub.api.ModelsApiDelegate;

public class AspectModelService implements ModelsApiDelegate {

   private final PersistenceLayer persistenceLayer;
   private final BammHelper bammHelper;

   public AspectModelService( final PersistenceLayer persistenceLayer, BammHelper bammHelper ) {
      this.persistenceLayer = persistenceLayer;
      this.bammHelper = bammHelper;
   }

   @Override
   public ResponseEntity<SemanticModelList> getModelList(Integer pageSize,
                                                         Integer page,
                                                         String namespaceFilter,
                                                         String nameFilter,
                                                         String nameType,
                                                         SemanticModelStatus status ) {

      try {
         String decodedType = null;
         if ( nameType != null ) {
            decodedType = URLDecoder.decode( nameType, StandardCharsets.UTF_8.name() );
         }
         final String decodedNamespace = URLDecoder.decode( namespaceFilter,
               StandardCharsets.UTF_8.name() );
         final String decodedName = java.net.URLDecoder.decode( nameFilter,
               StandardCharsets.UTF_8.name() );

         ModelPackageStatus modelPackageStatus = null;
         if ( status != null ) {
            modelPackageStatus = ModelPackageStatus.valueOf( status.name() );
         }
         final SemanticModelList list = persistenceLayer.getModels( decodedNamespace, decodedName, decodedType,
               modelPackageStatus, page,
               pageSize );

         return new ResponseEntity<>( list, HttpStatus.OK );
      } catch ( final java.io.UnsupportedEncodingException uee ) {
         return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
      }
   }

   @Override
   public ResponseEntity<SemanticModel> getModelByUrn(final String urn ) {
      final SemanticModel model = persistenceLayer.getModel( AspectModelUrn.fromUrn( urn ) );

      if ( model == null ) {
         return new ResponseEntity<>( HttpStatus.NOT_FOUND );
      }

      return new ResponseEntity<>( model, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<SemanticModel> createModelWithUrn(final SemanticModelType type, final String newModel, final SemanticModelStatus status ) {
      final SemanticModel resultingModel = persistenceLayer.save( type, newModel, status );
      return new ResponseEntity<>( resultingModel, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<org.springframework.core.io.Resource> getModelDiagram( final String urn ) {
      final VersionedModel versionedModel = getVersionedModel( urn );
      final byte[] pngBytes = bammHelper.generatePng( versionedModel );
      if ( pngBytes == null ) {
         throw new RuntimeException( String.format( "Failed to generate example payload for urn %s", urn ) );
      }
      return new ResponseEntity( pngBytes, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelJsonSchema( final String modelId ) {
      final JsonNode json = bammHelper.getJsonSchema( getBamAspect( modelId ) );
      return new ResponseEntity( json, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelDocu( final String modelId ) {
      VersionedModel versionedModel = getVersionedModel( modelId );
      final Try<byte[]> docuResult = bammHelper.getHtmlDocu( versionedModel );
      if ( docuResult.isFailure() ) {
         throw new RuntimeException( String.format( "Failed to generate documentation for urn %s", modelId ) );
      }
      final HttpHeaders headers = new HttpHeaders();
      headers.setContentType( MediaType.TEXT_HTML );
      return new ResponseEntity( docuResult.get(), headers, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelFile( final String modelId ) {
      String modelDefinition = persistenceLayer.getModelDefinition( AspectModelUrn.fromUrn( modelId ) );
      return new ResponseEntity( modelDefinition, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> deleteModel( final String modelId ) {
      persistenceLayer.deleteModelsPackage( ModelPackageUrn.fromUrn( modelId ) );
      return new ResponseEntity<>( HttpStatus.NO_CONTENT );
   }

   @Override
   public ResponseEntity<SemanticModel> modifyModel( final SemanticModelType type, final String newModel, final SemanticModelStatus status  ) {
      final SemanticModel resultingModel = persistenceLayer.save( type, newModel, status );
      return new ResponseEntity<>( resultingModel, HttpStatus.OK );
   }

   @Override
   public ResponseEntity<Void> getModelOpenApi( final String modelId, final String baseUrl ) {
      final Aspect bammAspect = getBamAspect( modelId );
      final String openApiJson = bammHelper.getOpenApiDefinitionJson( bammAspect, baseUrl );
      return new ResponseEntity( openApiJson, HttpStatus.OK );
   }

   private Aspect getBamAspect( String urn ) {
      final Try<Aspect> aspect = bammHelper.getAspectFromVersionedModel( getVersionedModel( urn ) );
      if ( aspect.isFailure() ) {
         throw new RuntimeException( "Failed to load aspect model", aspect.getCause() );
      }
      return aspect.get();
   }

   private VersionedModel getVersionedModel( String urn ) {
      final String modelDefinition = persistenceLayer.getModelDefinition( AspectModelUrn.fromUrn( urn ) );

      final Try<VersionedModel> versionedModel = bammHelper.loadBammModel( modelDefinition );

      if ( versionedModel.isFailure() ) {
         throw new RuntimeException( "Failed to load versioned model", versionedModel.getCause() );
      }
      return versionedModel.get();
   }

   @Override
   public ResponseEntity<Void> getModelExamplePayloadJson( final String modelId ) {
      final Aspect bammAspect = getBamAspect( modelId );
      final Try<String> result = bammHelper.getExamplePayloadJson( bammAspect );
      if ( result.isFailure() ) {
         throw new RuntimeException( String.format( "Failed to generate example payload for urn %s", modelId ) );
      }
      return new ResponseEntity( result.get(), HttpStatus.OK );
   }
}
