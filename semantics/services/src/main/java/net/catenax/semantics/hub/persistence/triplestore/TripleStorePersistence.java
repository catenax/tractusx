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
package net.catenax.semantics.hub.persistence.triplestore;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.update.UpdateRequest;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;
import net.catenax.semantics.hub.model.NewSemanticModel;
import net.catenax.semantics.hub.model.SemanticModel;
import net.catenax.semantics.hub.model.SemanticModelList;
import net.catenax.semantics.hub.model.SemanticModelStatus;
import net.catenax.semantics.hub.model.SemanticModelType;
import net.catenax.semantics.hub.persistence.PersistenceLayer;

public class TripleStorePersistence implements PersistenceLayer {

   private final RDFConnectionRemoteBuilder rdfConnectionRemoteBuilder;
   private final SdsSdk sdsSdk;

   public TripleStorePersistence( final RDFConnectionRemoteBuilder rdfConnectionRemoteBuilder,
         final SdsSdk sdsSdk ) {
      this.rdfConnectionRemoteBuilder = rdfConnectionRemoteBuilder;
      this.sdsSdk = sdsSdk;
   }

   @Override
   public SemanticModelList getModels( String namespaceFilter, String nameFilter, @Nullable String nameType,
         @Nullable String status, Integer page, Integer pageSize ) {
      final Query query = SparqlQueries.buildFindAllQuery( namespaceFilter, nameFilter, nameType, status, page,
            pageSize );
      final AtomicReference<List<SemanticModel>> aspectModels = new AtomicReference<>();
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.queryResultSet( query, resultSet -> {
            final List<QuerySolution> querySolutions = ResultSetFormatter.toList( resultSet );
            aspectModels.set( TripleStorePersistence.aspectModelFrom( querySolutions ) );
         } );
      }
      int totalSemanticModelCount = getTotalItemsCount();
      int totalPages = totalSemanticModelCount / pageSize;
      if ( totalPages == 0 ) {
         totalPages = 1;
      }
      SemanticModelList modelList = new SemanticModelList();
      List<SemanticModel> semanticModels = aspectModels.get();
      modelList.setCurrentPage( page );
      modelList.setItemCount( semanticModels.size() );
      modelList.setTotalPages( totalPages );
      modelList.setTotalItems( totalSemanticModelCount );
      modelList.setItems( aspectModels.get() );
      return modelList;
   }

   @Override
   public SemanticModel getModel( final AspectModelUrn urn ) {
      return findByUrn( urn );
   }

   @Override
   public Optional<SemanticModel> save( NewSemanticModel model ) {
      final Model rdfModel = sdsSdk.load( model.getModel().getBytes( StandardCharsets.UTF_8 ) );
      final AspectModelUrn modelUrn = sdsSdk.getAspectUrn( rdfModel );
      Optional<ModelsPackage> existsByPackage = findByPackageByUrn( ModelsPackageUrn.fromUrn( modelUrn ) );
      if ( existsByPackage.isPresent() ) {
         switch ( existsByPackage.get().getStatus() ) {
            case DRAFT:
               deleteByUrn( ModelsPackageUrn.fromUrn( modelUrn ) );
               break;
            case RELEASED:
               throw new IllegalArgumentException(
                     String.format( "The package %s is already in status RELEASE and cannot be modified.",
                           ModelsPackageUrn.fromUrn( modelUrn ).getUrn() ) );
         }
      }

      sdsSdk.validate( rdfModel, new SdsSdk.TripleStoreResolutionStrategy( this::findContainingModelByUrn ) );

      final Resource rootResource = ResourceFactory.createResource( modelUrn.getUrnPrefix() );
      rdfModel.add( rootResource, SparqlQueries.STATUS_PROPERTY,
            ModelsPackageStatus.valueOf( model.getStatus().name() ).toString() );

      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.update( new UpdateBuilder().addInsert( rdfModel ).build() );
      }

      return Optional.of( findByUrn( modelUrn ) );
   }


   @Override
   public Optional<String> getModelDefinition( final AspectModelUrn urn ) {
      Model jenaModelByUrn = findJenaModelByUrn( urn );
      StringWriter out = new StringWriter();
      jenaModelByUrn.write( out, "TURTLE" );
      String result = out.toString();
      return Optional.ofNullable( result );
   }


   @Override
   public void deleteModelsPackage( final ModelsPackageUrn urn ) {
      ModelsPackage modelsPackage = findByPackageByUrn( urn )
            .orElseThrow(
                  () -> new IllegalArgumentException( String.format( "Package for %s not found.", urn.getUrn() ) ) );

      if ( ModelsPackageStatus.RELEASED.equals( modelsPackage.getStatus() ) ) {
         throw new IllegalArgumentException(
               String.format( "The package %s is already in status RELEASE and cannot be modified.",
                     urn.getUrn() ) );
      }

      deleteByUrn( urn );
   }

   private Integer getTotalItemsCount() {
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         AtomicReference<Integer> count = new AtomicReference<>();
         rdfConnection.querySelect( SparqlQueries.buildCountAspectModelsQuery(), querySolution -> {
            int countResult = querySolution.getLiteral( "aspectModelCount" ).getInt();
            count.set( countResult );
         } );
         return count.get();
      }
   }

   private void deleteByUrn( final ModelsPackageUrn modelsPackage ) {
      final UpdateRequest deleteByUrn = SparqlQueries.buildDeleteByUrnRequest( modelsPackage );
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.update( deleteByUrn );
      }
   }

   private Model findContainingModelByUrn( final AspectModelUrn urn ) {
      final Query query = SparqlQueries.buildFindModelElementClosureQuery( urn );
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         return rdfConnection.queryConstruct( query );
      }
   }

   private Optional<ModelsPackage> findByPackageByUrn( ModelsPackageUrn modelsPackage ) {
      final Query query = SparqlQueries.buildFindByPackageQuery( modelsPackage );
      final AtomicReference<String> aspectModel = new AtomicReference<>();
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.querySelect( query,
               result -> aspectModel.set( result.get( SparqlQueries.STATUS_RESULT ).toString() ) );
      }
      if ( aspectModel.get() != null ) {
         return Optional.of( new ModelsPackage( ModelsPackageStatus.valueOf( aspectModel.get() ) ) );
      }
      return Optional.empty();
   }

   private SemanticModel findByUrn( final AspectModelUrn urn ) {
      final Query query = SparqlQueries.buildFindByUrnQuery( urn );
      final AtomicReference<SemanticModel> aspectModel = new AtomicReference<>();
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.querySelect( query,
               result -> aspectModel.set( TripleStorePersistence.aspectModelFrom( result ) ) );
      }
      return aspectModel.get();
   }


   private Model findJenaModelByUrn( final AspectModelUrn urn ) {
      final Query constructQuery = SparqlQueries.buildFindByUrnConstructQuery( urn );
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         return rdfConnection.queryConstruct( constructQuery );
      }
   }


   private static List<SemanticModel> aspectModelFrom(
         final List<QuerySolution> querySolutions ) {
      final Map<String, SemanticModel> aspectModels = new HashMap<>();
      querySolutions.stream()
                    .map( TripleStorePersistence::aspectModelFrom )
                    .forEach( aspectModel -> aspectModels.putIfAbsent( aspectModel.getName(), aspectModel ) );
      return new ArrayList<>( aspectModels.values() );
   }

   private static SemanticModel aspectModelFrom( final QuerySolution querySolution ) {
      final String urn = querySolution.get( SparqlQueries.ASPECT ).toString();
      final String status = querySolution.get( SparqlQueries.STATUS_RESULT ).toString();
      AspectModelUrn aspectModelUrn = AspectModelUrn.fromUrn( urn );
      SemanticModel model = new SemanticModel();
      model.setUrn( aspectModelUrn.getUrn().toString() );
      model.setType( SemanticModelType.BAMM );
      model.setVersion( aspectModelUrn.getVersion() );
      model.setName( aspectModelUrn.getName() );
      model.setStatus( SemanticModelStatus.fromValue( status ) );
      return model;
   }
}
