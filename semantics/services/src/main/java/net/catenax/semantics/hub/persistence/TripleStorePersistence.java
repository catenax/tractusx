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
package net.catenax.semantics.hub.persistence;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.jena.arq.querybuilder.UpdateBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;
import io.vavr.control.Try;
import net.catenax.semantics.hub.model.ModelList;
import net.catenax.semantics.hub.model.NewModel;
import net.catenax.semantics.hub.persistence.triplestore.SdsSdk;
import net.catenax.semantics.hub.persistence.triplestore.SparqlQueries;

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
      return null;
   }

   @Override
   public net.catenax.semantics.hub.model.Model getModel( final String modelId ) {
      return null;
   }

   @Override
   public Optional<net.catenax.semantics.hub.model.Model> insertNewModel( final NewModel model, final String id,
         final String version,
         final String name ) {
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
}
