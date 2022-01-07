/*
 * Copyright (c) 2022 Bosch Software Innovations GmbH. All rights reserved.
 */
package net.catenax.semantics.hub.persistence.triplestore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;
import io.vavr.control.Try;
import net.catenax.semantics.hub.model.ModelList;
import net.catenax.semantics.hub.model.NewModel;
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
   public ModelList getModels( final Boolean isPrivate, final String namespaceFilter, final String nameFilter,
         final String nameType,
         final String type, final String status, final int page, final int pageSize ) {
      final Query query = SparqlQueries.buildFindAllQuery();
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         final AtomicReference<List<net.catenax.semantics.hub.model.Model>> aspectModels = new AtomicReference<>();
         rdfConnection.queryResultSet( query, resultSet -> {
            final List<QuerySolution> querySolutions = ResultSetFormatter.toList( resultSet );
            aspectModels.set( TripleStorePersistence.aspectModelFrom( querySolutions ) );
         } );
         ModelList modelList = new ModelList();
         modelList.setItems( aspectModels.get() );
         return modelList;
      }
   }

   @Override
   public net.catenax.semantics.hub.model.Model getModel( final String modelId ) {
      return null;
   }

   @Override
   public Optional<net.catenax.semantics.hub.model.Model> insertNewModel( final NewModel model, final String id,
         final String version,
         final String name ) {
      throw new UnsupportedOperationException();
   }

   @Override
   public Optional<net.catenax.semantics.hub.model.Model> insertNewModel( NewModel model ) {
      final Model rdfModel = sdsSdk.load( model.getModel().getBytes( StandardCharsets.UTF_8 ) );

      // TODO :
      // Check if model already exists with status release. And deny request.
      sdsSdk.validate( rdfModel, new SdsSdk.TripleStoreResolutionStrategy( this::findContainingModelByUrn ) );

      final AspectModelUrn modelUrn = sdsSdk.getAspectUrn( rdfModel );
      final Resource rootResource = ResourceFactory.createResource( modelUrn.getUrnPrefix() );
      rdfModel.add( rootResource, SparqlQueries.STATUS_PROPERTY, "DRAFT" );

      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         rdfConnection.update( new UpdateBuilder().addInsert( rdfModel ).build() );
      }
      // TODO: set details to the model
      return Optional.of( new net.catenax.semantics.hub.model.Model() );
   }

   private Model findContainingModelByUrn( final AspectModelUrn urn ) {
      final Query query = SparqlQueries.buildFindModelElementClosureQuery( urn );
      try ( final RDFConnection rdfConnection = rdfConnectionRemoteBuilder.build() ) {
         return rdfConnection.queryConstruct( query );
      }
   }

   @Override
   public Optional<String> getModelDefinition( final String modelId ) {
      return null;
   }

   @Override
   public Try<Void> deleteModel( final String modelId ) {
      return null;
   }

   @Override
   public Optional<net.catenax.semantics.hub.model.Model> updateExistingModel( final NewModel model, final String id,
         final String version,
         final String name ) {
      return null;
   }

   private static List<net.catenax.semantics.hub.model.Model> aspectModelFrom(
         final List<QuerySolution> querySolutions ) {
      final Map<String, net.catenax.semantics.hub.model.Model> aspectModels = new HashMap<>();
      querySolutions.stream()
                    .map( TripleStorePersistence::aspectModelFrom )
                    .forEach( aspectModel -> aspectModels.putIfAbsent( aspectModel.getName(), aspectModel ) );
      return new ArrayList<>( aspectModels.values() );
   }

   private static net.catenax.semantics.hub.model.Model aspectModelFrom( final QuerySolution querySolution ) {
      final String urn = querySolution.get( SparqlQueries.ASPECT ).toString();
      final String status = querySolution.get( SparqlQueries.STATUS ).toString();
      AspectModelUrn aspectModelUrn = AspectModelUrn.fromUrn( urn );
      net.catenax.semantics.hub.model.Model model = new net.catenax.semantics.hub.model.Model();
      model.setType( net.catenax.semantics.hub.model.Model.TypeEnum.BAMM );
      model.setVersion( aspectModelUrn.getVersion() );
      model.setName( aspectModelUrn.getName() );
      model.setStatus( net.catenax.semantics.hub.model.Model.StatusEnum.fromValue( status ) );
      model._private( false );
      return model;
   }
}
