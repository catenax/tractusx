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

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.update.UpdateRequest;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;

public class SparqlQueries {
   private static final String AUXILIARY_NAMESPACE = "urn:bamm:io.openmanufacturing:aspect-model:aux#";

   private static final String BAMM_NAMESPACE = "urn:bamm:io.openmanufacturing:meta-model:1.0.0#";
   private static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

   public static final String ASPECT = "aspect";
   public static final String BAMM_ASPECT_URN_REGEX = "urn:bamm:io.openmanufacturing:meta-model:\\d\\.\\d\\.\\d#Aspect";
   public static final Property STATUS_PROPERTY = ResourceFactory.createProperty( AUXILIARY_NAMESPACE, "status" );

   private static final String DELETE_BY_URN_QUERY =
         "DELETE { ?aspect ?p ?o . } \n"
               + "WHERE \n"
               + "{ \n"
               + "    ?aspect a ?bammAspect ;\n"
               + "    bind( $urnParam as ?urn )\n"
               + "    bind( $bammAspectUrnParam as ?bammAspectUrn )\n"
               + "    FILTER ( ( str(?aspect) = ?urn )\n"
               + "            && regex(str(?bammAspect), ?bammAspectUrn, \"\") )\n"
               + "    ?aspect ?p ?o .\n"
               + "}\n";

   private static final String CONSTRUCT_BY_URN_QUERY =
         "CONSTRUCT {\n"
               + " ?s ?p ?o .\n"
               + "} WHERE {\n"
               + "    bind( ns: as ?aspect )\n"
               + "    ?aspect (<>|!<>)* ?s .\n"
               + "    ?otherAspect (<>|!<>)* ?s .\n"
               + "    ?s ?p ?o .\n"
               + "}";

   private static final String FIND_MODEL_ELEMENT_CLOSURE =
         "CONSTRUCT {\n"
               + " ?s ?p ?o .\n"
               + "} WHERE {\n"
               + "    ?aspect (<>|!<>)* ?s .\n"
               + "    OPTIONAL {\n"
               + "        ?otherAspect (<>|!<>)* ?s .\n"
               + "    }\n"
               + "    ?s ?p ?o .\n"
               + "}";

   private static final String FIND_BY_URN_QUERY =
         "SELECT  *\n"
               + "WHERE\n"
               + "  {\n"
               + "      bind( $urnParam as ?urn ) \n"
               + "  }";

   private static final String FIND_ALL_QUERY =
         "SELECT  *\n"
               + "WHERE \n"
               + "{ \n"
               + "    ?aspect a ?bammAspect ;\n"
               + "    bind( $bammAspectUrnParam as ?bammAspectUrn )\n"
               + "    FILTER ( regex(str(?bammAspect), ?bammAspectUrn, \"\") )\n"
               + "    ?aspect ?p ?o .\n"
               + "}\n";

   private SparqlQueries() {
   }

   public static Query buildFindByUrnQuery( final AspectModelUrn urn ) {
      final ParameterizedSparqlString pss = create( FIND_BY_URN_QUERY );
      pss.setLiteral( "$urnParam", urn.toString() );
      return pss.asQuery();
   }

   public static UpdateRequest buildDeleteByUrnRequest( final AspectModelUrn urn ) {
      final ParameterizedSparqlString pss = create( DELETE_BY_URN_QUERY );
      pss.setLiteral( "$urnParam", urn.toString() );
      pss.setLiteral( "$bammAspectUrnParam", BAMM_ASPECT_URN_REGEX );
      return pss.asUpdate();
   }

   public static Query buildFindAllQuery() {
      final ParameterizedSparqlString pss = create( FIND_ALL_QUERY );
      pss.setLiteral( "$bammAspectUrnParam", BAMM_ASPECT_URN_REGEX );
      return pss.asQuery();
   }

   public static Query buildFindByUrnConstructQuery( final AspectModelUrn urn ) {
      final ParameterizedSparqlString pss = create( CONSTRUCT_BY_URN_QUERY );
      pss.setNsPrefix( "ns", urn.getUrn().toString() );
      return pss.asQuery();
   }

   public static Query buildFindModelElementClosureQuery( final AspectModelUrn urn ) {
      final ParameterizedSparqlString pss = create( FIND_MODEL_ELEMENT_CLOSURE );
      pss.setNsPrefix( "ns", urn.getUrn().toString() );
      return pss.asQuery();
   }

   private static ParameterizedSparqlString create( final String query ) {
      final ParameterizedSparqlString pss = new ParameterizedSparqlString();
      pss.setCommandText( query );
      pss.setNsPrefix( "aux", AUXILIARY_NAMESPACE );
      return pss;
   }
}
