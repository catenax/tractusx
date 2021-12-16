/*
 * Copyright (c) 2021 Robert Bosch Manufacturing Solutions GmbH
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

package net.catenax.semantics.registry;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import com.jayway.jsonpath.JsonPath;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class TwinsApiTest {

   @Autowired
   private MockMvc mockMvc;

   private final BasicJsonTester json = new BasicJsonTester( getClass() );

   @Test
   public void testGetTwinsExpect200() throws Exception {
      this.mockMvc.perform( get( "/api/v1/twins" ) )
                  .andDo( print() )
                  .andExpect( status().isOk() )
                  .andExpect( jsonPath( "$.items" ).exists() )
                  .andExpect( jsonPath( "$.totalItems" ).exists() );
   }

   @Test
   public void testDeleteTwinByNotExistingIdExpect404() throws Exception {
      this.mockMvc.perform( delete( "/api/v1/twins/{twinId}", UUID.randomUUID() ) )
                  .andDo( print() )
                  .andExpect( status().isNotFound() );
   }

   @Test
   public void testCreateValidTwinExpect200() throws Exception {
      final var body = json.from( "valid_twin.json" ).getJson();
      this.mockMvc.perform( post( "/api/v1/twins" ).content( body )
                                            .contentType( MediaType.APPLICATION_JSON ) )
                  .andDo( print() )
                  .andExpect( status().isOk() )
                  .andExpect( jsonPath( "[0].id" ).exists() );
   }

   @Test
   public void testGetTwinByIdExpect200() throws Exception {
      final var body = json.from( "valid_twin.json" ).getJson();
      final var twinsResponse = this.mockMvc.perform( post( "/api/v1/twins" ).content( body )
                                                                      .contentType( MediaType.APPLICATION_JSON ) )
                                            .andExpect( status().isOk() )
                                            .andExpect( jsonPath( "[0].id" ).exists() )
                                            .andReturn().getResponse().getContentAsString();
      final var twinId = JsonPath.read( twinsResponse, "[0].id" );
      this.mockMvc.perform( get( "/api/v1/twins/{twinId}", twinId ) )
                  .andDo( print() )
                  .andExpect( status().isOk() )
                  .andExpect( jsonPath( "$.id" ).value( twinId ) );
   }

   @Test
   public void testGetTwinByLocalIdentifierExpect200() throws Exception {
      final var body = json.from( "valid_twin_with_real_values.json" ).getJson();
      final var twinsResponse = this.mockMvc.perform( post( "/api/v1/twins" ).content( body )
                                                                      .contentType( MediaType.APPLICATION_JSON ) )
                                            .andExpect( status().isOk() )
                                            .andExpect( jsonPath( "[0].id" ).exists() )
                                            .andReturn().getResponse().getContentAsString();
      final var twinId = JsonPath.read( twinsResponse, "[0].id" );
      final var key = "VIN";
      final var value = "WXFFJDKF10DFJA";

      this.mockMvc
            .perform( get( "/api/v1/twins/?key={key}&value={value}", key, value ) )
            .andDo( print() )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.items", hasSize( 1 ) ) )
            .andExpect( jsonPath( "$.items[0].id" ).value( twinId ) )
            .andExpect( jsonPath( "$.items[0].manufacturer" ).value( "Manufacturer XYZ" ) )
            .andExpect( jsonPath( "$.items[0].description" ).value( "This is a test twin." ) )
            .andExpect( jsonPath( "$.items[0].localIdentifiers[0].key" ).value( key ) )
            .andExpect( jsonPath( "$.items[0].localIdentifiers[0].value" ).value( value ) )
            .andExpect( jsonPath( "$.items[0].aspects[0].httpEndpoints[0].method" ).value( "POST" ) )
            .andExpect(
                  jsonPath( "$.items[0].aspects[0].httpEndpoints[0].url" ).value( "https://testaspect.com/data" ) )
            .andExpect( jsonPath( "$.items[0].aspects[0].modelReference.urn" ).value(
                  "urn:bamm:io.openmanufacturing:1.0.0:TestAspect" ) );
   }

   @Test
   public void testBatchApi() throws Exception {
      final var body = json.from( "valid_twins_with_multiple_local_ids.json" ).getJson();
      final var twinsResponse = this.mockMvc.perform( post( "/api/v1/twins" ).content( body )
                                                                      .contentType( MediaType.APPLICATION_JSON ) )
                                            .andExpect( status().isOk() )
                                            .andExpect( jsonPath( "[0].id" ).exists() )
                                            .andReturn().getResponse().getContentAsString();


      final String requestBody = json.from( "batch-request.json" ).getJson();

      this.mockMvc
         .perform( post("/api/v1/twins/fetch")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
         )
         .andExpect( jsonPath("[3].localIdentifiers.[0].key").value("abc5") )
         .andExpect( jsonPath("[2].localIdentifiers.[0].key").value("abc1") )
         .andExpect( jsonPath("[1].localIdentifiers.[0].key").value("abc1") )
         .andExpect( jsonPath("[0].localIdentifiers.[0].key").value("def1") )
         .andExpect( jsonPath("[0].localIdentifiers.[1].key").value("ghi1") );
   }

   @Test
   public void testBatchApiPerformance() throws UnsupportedEncodingException, Exception {
      final int numTwinsInDB = 100000;
      final int fetchRequestSize = 100;

      String genericTwinString = json.from("generic_valid_twin.json").getJson();
      JSONObject genericTwin = new JSONObject(genericTwinString);

      JSONArray insertTwinsRequest = new JSONArray();
      
      for(int i = 0; i < numTwinsInDB; i++) {
         JSONObject clonedTwin = new JSONObject(genericTwin.toString());

         clonedTwin.getJSONArray("localIdentifiers").getJSONObject(0).put("key", "key" + i);
         clonedTwin.getJSONArray("localIdentifiers").getJSONObject(0).put("value", "123456789-" + i);

         insertTwinsRequest.put(clonedTwin);
      }
      log.info("Inserting test twins...");

      final var twinsResponse = this.mockMvc.perform( post( "/api/v1/twins" ).content( insertTwinsRequest.toString() )
                                                                      .contentType( MediaType.APPLICATION_JSON ) )
                                            .andExpect( status().isOk() )
                                            .andExpect( jsonPath( "[0].id" ).exists() )
                                            .andReturn().getResponse().getContentAsString();

      log.info("Finished insert");
      
      JSONArray identifiers = new JSONArray();
      
      for(int i = 0; i < fetchRequestSize; i++) {
         identifiers.put(new JSONObject()
            .put("key", "key" + i*(numTwinsInDB/fetchRequestSize))
            .put("value", "123456789-" + i*(numTwinsInDB/fetchRequestSize))
         );
      }

      JSONObject batchRequest = new JSONObject()
         .put("identifiers", identifiers);

      long start = System.currentTimeMillis();
      
      String resultString = this.mockMvc
         .perform( post("/api/v1/twins/fetch")
            .content(batchRequest.toString())
            .contentType(MediaType.APPLICATION_JSON)
         )
         .andReturn().getResponse().getContentAsString();

      JSONArray parsedResult = new JSONArray(resultString);

      long end = System.currentTimeMillis();
      log.info("Fetching " + parsedResult.length() + " twins out of " + numTwinsInDB + " in the database. The request took " + (end - start) + " Milliseconds.");
   }
}
