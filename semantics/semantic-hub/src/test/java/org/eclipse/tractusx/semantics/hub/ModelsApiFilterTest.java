/********************************************************************************
 * Copyright (c) 2021-2022 Robert Bosch Manufacturing Solutions GmbH
 * Copyright (c) 2021-2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.semantics.hub;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the models filter api with different filter combinations.
 * The Fuseki Server is cleared with @DirtiesContext and ensures test runs on a fresh Fuseki Server.
 */
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_CLASS )
public class ModelsApiFilterTest extends AbstractModelsApiTest{

   @BeforeAll
   public void init() throws Exception {
      createModel( TestUtils.TRACEABILITY_MODEL_PATH, "RELEASED" );
      createModel( TestUtils.MODEL_WITH_REFERENCE_TO_TRACEABILITY_MODEL_PATH, "DRAFT" );
   }

   @Test
   public void testGetByNamespaceExpectResultsFound() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?namespaceFilter=urn:bamm:org.eclipse" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items.length()" ).value( 2 ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 2 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 2 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?namespaceFilter=urn:bamm:org.eclipse.tractusx.traceability" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items.length()" ).value( 1 ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 1 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 1 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );
   }

   @Test
   public void testGetModelListByNotAvailablePropertyTypeExpectEmptyResult() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?nameType=bamm:SingleEntity&nameFilter=SpatialPositionCharacteristic" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items" ).isEmpty() )
         .andExpect( jsonPath( "$.totalItems", equalTo( 0 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 0 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );
   }

   @Test
   public void testGetModelListByAvailablePropertyTypeExpectResultsFound() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?nameType=bamm:Property&nameFilter=Static%20Data" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items.length()" ).value( 2 ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 2 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 2 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );
   }

   @Test
   public void testGetByCombinedFilters() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?namespaceFilter=urn:bamm:org.eclipse.tractusx.traceability&nameType=bamm:Property&nameFilter=Static%20Data" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items.length()" ).value( 1 ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 1 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 1 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?namespaceFilter=urn:bamm:org.eclipse.tractusx.modelwithreferencetotraceability&nameType=bamm:Property&nameFilter=Individual%20Data" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items.length()" ).value( 0 ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 0 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 0 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );
   }

   @Test
   public void testGetModelListByDescriptionExpectSuccess() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.get(
                                           "/api/v1/models?nameType=_DESCRIPTION_&nameFilter=This%20model%20references" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items.length()" ).value( 1 ) )
         .andExpect( jsonPath( "$.items[*].urn", hasItem(
               "urn:bamm:org.eclipse.tractusx.modelwithreferencetotraceability:0.1.1#ModelWithReferenceToTraceability" ) ) )
         .andExpect( jsonPath( "$.items[*].version", hasItem( "0.1.1" ) ) )
         .andExpect( jsonPath( "$.items[*].name", hasItem( "ModelWithReferenceToTraceability" ) ) )
         .andExpect( jsonPath( "$.items[*].type", hasItem( "BAMM" ) ) )
         .andExpect( jsonPath( "$.items[*].status", hasItem( "DRAFT" ) ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 1 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 1 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );
   }

   @Test
   public void testGetModelListByStatusExpectSuccess() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models?status=DRAFT" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items[*].urn", hasItem(
               "urn:bamm:org.eclipse.tractusx.modelwithreferencetotraceability:0.1.1#ModelWithReferenceToTraceability" ) ) )
         .andExpect( jsonPath( "$.items[*].version", hasItem( "0.1.1" ) ) )
         .andExpect( jsonPath( "$.items[*].name", hasItem( "ModelWithReferenceToTraceability" ) ) )
         .andExpect( jsonPath( "$.items[*].type", hasItem( "BAMM" ) ) )
         .andExpect( jsonPath( "$.items[*].status", hasItem( "DRAFT" ) ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 1 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 1 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models?status=RELEASED" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items[*].urn", hasItem(
               "urn:bamm:org.eclipse.tractusx.traceability:0.1.1#Traceability" ) ) )
         .andExpect( jsonPath( "$.items[*].version", hasItem( "0.1.1" ) ) )
         .andExpect( jsonPath( "$.items[*].name", hasItem( "Traceability" ) ) )
         .andExpect( jsonPath( "$.items[*].type", hasItem( "BAMM" ) ) )
         .andExpect( jsonPath( "$.items[*].status", hasItem( "RELEASED" ) ) )
         .andExpect( jsonPath( "$.totalItems", equalTo( 1 ) ) )
         .andExpect( jsonPath( "$.itemCount", equalTo( 1 ) ) )
         .andExpect( MockMvcResultMatchers.status().isOk() );
   }

   private void createModel( String fileName, String status ) throws Exception {
      String modelWithReferenceToTraceability = TestUtils.loadModelFromResources( fileName );
      mvc.perform(
               MockMvcRequestBuilders
                     .post( "/api/v1/models" )
                     .queryParam("status", status)
                     .queryParam("type", "BAMM")
                     .accept( MediaType.APPLICATION_JSON )
                     .contentType( MediaType.TEXT_PLAIN )
                     .content(modelWithReferenceToTraceability)
                     .with(jwtTokenFactory.allRoles())
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );
   }
}
