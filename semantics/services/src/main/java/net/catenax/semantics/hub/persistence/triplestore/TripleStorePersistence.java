/*
 * Copyright (c) 2022 Bosch Software Innovations GmbH. All rights reserved.
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
import io.vavr.control.Try;
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
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         final AtomicReference<List<SemanticModel>> aspectModels = new AtomicReference<>();
         rdfConnection.queryResultSet( query, resultSet -> {
            final List<QuerySolution> querySolutions = ResultSetFormatter.toList( resultSet );
            aspectModels.set( TripleStorePersistence.aspectModelFrom( querySolutions ) );
         } );
         SemanticModelList modelList = new SemanticModelList();
         modelList.setItems( aspectModels.get() );
         return modelList;
      }
   }

   @Override
   public SemanticModel getModel( final String modelId ) {
      return null;
   }

   @Override
   public Optional<SemanticModel> insertNewModel( NewSemanticModel model ) {
      final Model rdfModel = sdsSdk.load( model.getModel().getBytes( StandardCharsets.UTF_8 ) );

      final AspectModelUrn modelUrn = sdsSdk.getAspectUrn( rdfModel );
      Optional<String> existsByPackage = Optional.ofNullable(
            findByPackage( ModelsPackage.from( modelUrn ) ) );
      if ( existsByPackage.isPresent() ) {
         SemanticModelStatus status = SemanticModelStatus.valueOf( existsByPackage.get() );
         switch ( status ) {
            case DRAFT:
               deleteByUrn( ModelsPackage.from( modelUrn ) );
               break;
            case RELEASED:
               throw new IllegalArgumentException(
                     String.format( "The package %s is already in status RELEASE and cannot be modified.",
                           ModelsPackage.from( modelUrn ).getUrn() ) );
         }
      }

      sdsSdk.validate( rdfModel, new SdsSdk.TripleStoreResolutionStrategy( this::findContainingModelByUrn ) );

      final Resource rootResource = ResourceFactory.createResource( modelUrn.getUrnPrefix() );
      rdfModel.add( rootResource, SparqlQueries.STATUS_PROPERTY, model.getStatus().getValue() );

      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.update( new UpdateBuilder().addInsert( rdfModel ).build() );
      }

      return Optional.of( findByUrn( modelUrn ) );
   }

   private void deleteByUrn( final ModelsPackage modelsPackage ) {
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

   private String findByPackage( ModelsPackage modelsPackage ) {
      final Query query = SparqlQueries.buildFindByPackageQuery( modelsPackage );
      final AtomicReference<String> aspectModel = new AtomicReference<>();
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.querySelect( query,
               result -> aspectModel.set( result.get( SparqlQueries.STATUS ).toString() ) );
      }
      return aspectModel.get();
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

   @Override
   public Optional<String> getModelDefinition( final String modelId ) {
      Model jenaModelByUrn = findJenaModelByUrn( AspectModelUrn.fromUrn( modelId ) );
      StringWriter out = new StringWriter();
      jenaModelByUrn.write( out, "TURTLE" );
      String result = out.toString();
      return Optional.ofNullable( result );
   }

   private Model findJenaModelByUrn( final AspectModelUrn urn ) {
      final Query constructQuery = SparqlQueries.buildFindByUrnConstructQuery( urn );
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         return rdfConnection.queryConstruct( constructQuery );
      }
   }

   @Override
   public Try<Void> deleteModel( final String modelId ) {
      return null;
   }

   @Override
   public Optional<SemanticModel> updateExistingModel( final NewSemanticModel model, final String id,
         final String version,
         final String name ) {
      return null;
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
      final String status = querySolution.get( SparqlQueries.STATUS ).toString();
      AspectModelUrn aspectModelUrn = AspectModelUrn.fromUrn( urn );
      SemanticModel model = new SemanticModel();
      model.setType( SemanticModelType.BAMM );
      model.setVersion( aspectModelUrn.getVersion() );
      model.setName( aspectModelUrn.getName() );
      model.setStatus( SemanticModelStatus.fromValue( status ) );
      return model;
   }
}
