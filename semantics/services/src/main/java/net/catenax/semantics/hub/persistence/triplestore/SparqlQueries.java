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
import net.catenax.semantics.hub.domain.ModelPackageStatus;
import net.catenax.semantics.hub.domain.ModelPackageUrn;

public class SparqlQueries {
   private static final String AUXILIARY_NAMESPACE = "urn:bamm:io.openmanufacturing:aspect-model:aux#";

   public static final String ASPECT = "aspect";
   public static final String STATUS_RESULT = "statusResult";
   public static final String BAMM_ASPECT_URN_REGEX = "urn:bamm:io.openmanufacturing:meta-model:\\d\\.\\d\\.\\d#Aspect";
   public static final String BAMM_ASPECT_URN_PREFIX = "urn:bamm:io.openmanufacturing:meta-model:\\d\\.\\d\\.\\d#";
   public static final String BAMM_PREFERRED_NAME = "urn:bamm:io.openmanufacturing:meta-model:1.0.0#preferredName";
   public static final String BAMM_DESCRIPTION = "urn:bamm:io.openmanufacturing:meta-model:1.0.0#description";
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

   /**
    * This query finds all elements belonging to the provided urn
    */
   private static final String FIND_MODEL_ELEMENT_CLOSURE =
         "CONSTRUCT {\n"
               + " ?s ?p ?o .\n"
               + "} WHERE {\n"
               + "    bind( ns: as ?urn )\n"
               + "    ?urn (<>|!<>)* ?s .\n"
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
    * To ensures accurate page information results,
    * the where clause have to be used for the find all query and the count query.
    */
   private static final String FILTER_QUERY_WHERE_CLAUSE = "WHERE\n"
         + "{ \n"
         + "  \n"
         + "     BIND($bammAspectUrnRegexParam AS ?bammAspectUrnRegex)\n"
         + "     BIND($bammTypeUrnRegexParam AS ?bammTypeUrnRegex)\n"
         + "     BIND(iri($bammFieldToSearchInParam) AS ?bammFieldToSearchIn)\n"
         + "     BIND($bammFieldSearchValueParam AS ?bammFieldSearchValue)\n"
         + "     BIND($statusFilterParam AS ?statusFilter)\n"
         + "     BIND($namespaceFilterParam AS ?namespaceFilter)\n"
         + "     ?s  a  ?bammType\n"
         + "     FILTER ( !bound(?bammTypeUrnRegex) || regex(str(?bammType), ?bammTypeUrnRegex, \"\") )\n"
         + "     ?s  ?bammFieldToSearchIn  ?bammFieldContent\n"
         + "     FILTER ( !bound(?bammFieldSearchValue) || contains(str(?bammFieldContent), ?bammFieldSearchValue) )\n"
         + "     ?aspect  a ?bammAspect .  \n"
         //      selects nodes having a reference to ?s
         + "     ?aspect (<>|!<>)* ?s . \n"
         //      filters the result to match only bamm aspects
         + "     FILTER regex(str(?bammAspect), ?bammAspectUrnRegex, \"\")\n"
         + "     BIND(iri(concat(strbefore(str(?aspect ), \"#\"), \"#\")) AS ?package)\n"
         + "     ?package  aux:status  ?status\n"
         + "     FILTER (  !bound(?statusFilter) || contains(str(?status), ?statusFilter) )\n"
         + "     FILTER (  !bound(?namespaceFilter) || contains(str(?aspect), ?namespaceFilter ) )\n"
         + "  }\n";

   private static final String FIND_ALL_EXTENDED_QUERY =
         "SELECT DISTINCT ?aspect (?status as ?statusResult)\n"
               + FILTER_QUERY_WHERE_CLAUSE
               + "ORDER BY lcase(str(?aspect))\n"
               + "OFFSET  0\n"
               + "LIMIT   10";

   private static final String COUNT_ASPECT_MODELS_QUERY =
         "SELECT (count(DISTINCT ?aspect) as ?aspectModelCount)\n"
               + FILTER_QUERY_WHERE_CLAUSE;

   private SparqlQueries() {
   }

   public static Query buildFindByUrnQuery( final AspectModelUrn urn ) {
      final ParameterizedSparqlString pss = create( FIND_BY_URN_QUERY );
      pss.setLiteral( "$urnParam", urn.toString() );
      pss.setLiteral( "$bammAspectUrnParam", BAMM_ASPECT_URN_REGEX );
      pss.setLiteral( "$packageUrnParam", ModelPackageUrn.fromUrn( urn ).getUrn() );
      return pss.asQuery();
   }

   public static Query buildCountAspectModelsQuery( String namespaceFilter, String nameFilter, String nameType,
         ModelPackageStatus status ) {
      return buildQueryWithSearchFilters( COUNT_ASPECT_MODELS_QUERY, namespaceFilter,
            nameFilter, nameType, status ).asQuery();
   }

   public static Query buildFindByPackageQuery( final ModelPackageUrn modelsPackage ) {
      final ParameterizedSparqlString pss = create( FIND_BY_PACKAGE_URN_QUERY );
      pss.setLiteral( "$urnParam", modelsPackage.getUrn() );
      return pss.asQuery();
   }

   public static UpdateRequest buildDeleteByUrnRequest( final ModelPackageUrn modelsPackage ) {
      final ParameterizedSparqlString pss = create( DELETE_BY_URN_QUERY );
      pss.setLiteral( "$urnParam", modelsPackage.getUrn() );
      return pss.asUpdate();
   }

   public static Query buildFindAllQuery( String namespaceFilter, String nameFilter, String nameType, ModelPackageStatus status,
         int page, int pageSize ) {
      final ParameterizedSparqlString pss = buildQueryWithSearchFilters( FIND_ALL_EXTENDED_QUERY, namespaceFilter,
            nameFilter, nameType, status );
      pss.setLiteral( "$limitParam", pageSize );
      pss.setLiteral( "$offsetParam", getOffset( page, pageSize ) );
      return pss.asQuery();
   }

   private static ParameterizedSparqlString buildQueryWithSearchFilters( String query, String namespaceFilter,
         String nameFilter, String nameType,
         ModelPackageStatus status ) {
      final ParameterizedSparqlString pss = create( query );
      pss.setLiteral( "$bammAspectUrnRegexParam", BAMM_ASPECT_URN_REGEX );
      boolean nameFilterExists = StringUtils.isNotBlank( nameFilter );

      if ( "_NAME_".equals( nameType ) && nameFilterExists ) {
         pss.setLiteral( "$bammFieldToSearchInParam", BAMM_PREFERRED_NAME );
         pss.setLiteral( "$bammFieldSearchValueParam", nameFilter );
      } else if ( "_DESCRIPTION_".equals( nameType ) && nameFilterExists ) {
         pss.setLiteral( "$bammFieldToSearchInParam", BAMM_DESCRIPTION );
         pss.setLiteral( "$bammFieldSearchValueParam", nameFilter );
      } else if ( StringUtils.isNotBlank( nameType ) && nameFilterExists ) {
         pss.setLiteral( "$bammTypeUrnRegexParam", BAMM_ASPECT_URN_PREFIX + nameType.replace( "bamm:", "" ).strip() );
         pss.setLiteral( "$bammFieldToSearchInParam", BAMM_PREFERRED_NAME );
         pss.setLiteral( "$bammFieldSearchValueParam", nameFilter );
      } else {
         pss.setLiteral( "$bammFieldToSearchInParam", BAMM_PREFERRED_NAME );
         pss.setLiteral( "$bammTypeUrnRegexParam", BAMM_ASPECT_URN_REGEX );
      }
      if ( status != null ) {
         pss.setLiteral( "$statusFilterParam", status.name() );
      }
      if ( StringUtils.isNotBlank( namespaceFilter ) ) {
         pss.setLiteral( "$namespaceFilterParam", namespaceFilter );
      }
      return pss;
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
