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

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.update.UpdateRequest;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;
import net.catenax.semantics.hub.domain.ModelsPackageUrn;

public class SparqlQueries {
   private static final String AUXILIARY_NAMESPACE = "urn:bamm:io.openmanufacturing:aspect-model:aux#";

   public static final String ASPECT = "aspect";
   public static final String STATUS_RESULT = "statusResult";
   public static final String BAMM_ASPECT_URN_REGEX = "urn:bamm:io.openmanufacturing:meta-model:\\d\\.\\d\\.\\d#Aspect";
   public static final Property STATUS_PROPERTY = ResourceFactory.createProperty( AUXILIARY_NAMESPACE, "status" );

   private static final String DELETE_BY_URN_QUERY =
         "DELETE { ?s ?p ?o . } \n"
               + "WHERE \n"
               + "{ \n"
               + "    ?s ?p ?o ;\n"
               + "    bind( $urnParam as ?urn )\n"
               + "    FILTER ( strstarts(str(?s), ?urn) )\n"
               + "    ?s ?p ?o .\n"
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
         "SELECT  ?aspect (?status as ?statusResult)\n"
               + "WHERE\n"
               + "  {\n"
               + "      bind( $urnParam as ?urn ) \n"
               + "      bind( $packageUrnParam as ?packageUrn ) \n"
               + "      bind( $bammAspectUrnParam as ?bammAspectUrn )\n"
               + "      ?aspect a ?bammAspect .\n"
               + "      ?s aux:status ?status .\n"
               + "      FILTER ( regex(str(?bammAspect), ?bammAspectUrn, \"\") && ( str(?aspect) = ?urn )\n"
               + "            && (str(?s) = ?packageUrn) )\n"
               + "  }";

   private static final String FIND_BY_PACKAGE_URN_QUERY =
         "SELECT (?status as ?statusResult)\n"
               + "WHERE\n"
               + "  {\n"
               + "      bind( $urnParam as ?urn ) \n"
               + "      ?s aux:status ?status .\n"
               + "      FILTER ( ( str(?s) = ?urn ) )\n"
               + "      ?s aux:status ?status .\n"
               + "  }";

   /**
    * Returns all available aspects and their status by the provided page and offset.
    */
   private static final String FIND_ALL_QUERY =
         "SELECT ?aspect (?status as ?statusResult)\n"
               + "WHERE \n"
               + "{ \n"
               + "    ?aspect a ?bammAspect .\n"
               + "    bind( $bammAspectUrnParam as ?bammAspectUrn )\n"
               + "    FILTER ( regex(str(?bammAspect), ?bammAspectUrn, \"\") )\n"
               // The status is bound to the package. The below function extracts the iri of the package from the found aspect.
               + "    bind(iri(concat(strbefore(str(?aspect),\"#\"),\"#\")) as ?package)\n"
               + "    ?package aux:status ?status ;\n"
               + "}\n"
               + "    ORDER BY (LCASE(str(?aspect)))\n"
               + "    OFFSET $offsetParam\n"
               + "    LIMIT $limitParam\n";

   private static final String COUNT_BY_ASPECT_MODELS_QUERY =
         "SELECT (count(DISTINCT ?aspect) as ?aspectModelCount)\n"
               + "WHERE {\n"
               + "    ?aspect a ?bammAspect .\n"
               + "    bind( $bammAspectUrnParam as ?bammAspectUrn )\n"
               + "    FILTER ( regex(str(?bammAspect), ?bammAspectUrn, \"\") )\n"
               + "    ?aspect ?p ?o .\n"
               + "}";

   private SparqlQueries() {
   }

   public static Query buildFindByUrnQuery( final AspectModelUrn urn ) {
      final ParameterizedSparqlString pss = create( FIND_BY_URN_QUERY );
      pss.setLiteral( "$urnParam", urn.toString() );
      pss.setLiteral( "$bammAspectUrnParam", BAMM_ASPECT_URN_REGEX );
      pss.setLiteral( "$packageUrnParam", ModelsPackageUrn.fromUrn( urn ).getUrn() );
      return pss.asQuery();
   }

   public static Query buildCountAspectModelsQuery() {
      final ParameterizedSparqlString pss = create( COUNT_BY_ASPECT_MODELS_QUERY );
      pss.setLiteral( "$bammAspectUrnParam", BAMM_ASPECT_URN_REGEX );
      return pss.asQuery();
   }

   public static Query buildFindByPackageQuery( final ModelsPackageUrn modelsPackage ) {
      final ParameterizedSparqlString pss = create( FIND_BY_PACKAGE_URN_QUERY );
      pss.setLiteral( "$urnParam", modelsPackage.getUrn() );
      return pss.asQuery();
   }

   public static UpdateRequest buildDeleteByUrnRequest( final ModelsPackageUrn modelsPackage ) {
      final ParameterizedSparqlString pss = create( DELETE_BY_URN_QUERY );
      pss.setLiteral( "$urnParam", modelsPackage.getUrn() );
      return pss.asUpdate();
   }

   public static Query buildFindAllQuery( String namespaceFilter, String nameType, String type, String status,
         int page, int pageSize ) {
      // TODO implement sparql query
      final ParameterizedSparqlString pss = create( FIND_ALL_QUERY );
      pss.setLiteral( "$bammAspectUrnParam", BAMM_ASPECT_URN_REGEX );
      if ( StringUtils.isNotBlank( namespaceFilter ) ) {
         pss.setLiteral( "$nameSpaceFilterParam", namespaceFilter );
      }
      if ( StringUtils.isNotBlank( nameType ) ) {
         pss.setLiteral( "$nameTypeParam", nameType );
      }
      if ( StringUtils.isNotBlank( status ) ) {
         pss.setLiteral( "$statusParam", status );
      }
      if ( StringUtils.isNotBlank( type ) ) {
         pss.setLiteral( "$typeParam", type );
      }
      pss.setLiteral( "$limitParam", pageSize );
      pss.setLiteral( "$offsetParam", getOffset( page, pageSize ) );
      return pss.asQuery();
   }

   private static Integer getOffset( int page, int pageSize ) {
      if ( page <= 1 ) {
         return 0;
      }
      return (page - 1) * pageSize;
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
