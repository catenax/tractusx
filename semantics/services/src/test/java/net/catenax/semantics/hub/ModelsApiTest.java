package net.catenax.semantics.hub;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext( classMode = DirtiesContext.ClassMode.AFTER_CLASS )
public class ModelsApiTest {
   @Autowired
   private MockMvc mvc;

   @BeforeAll
   static public void init() {
   }

   @Test
   public void testGetModelsExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax:1.0.0#> .\\n \\n :Movement a bamm:Aspect;\\n bamm:name \\\"Movement\\\";\\n bamm:preferredName \\\"Movement\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"DRAFT\"}";
      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models" )
                                     .accept( MediaType.APPLICATION_JSON )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.items" ).isArray() )
         .andExpect( jsonPath( "$.items[*].urn", hasItem( "urn:bamm:net.catenax:1.0.0#Movement" ) ) )
         .andExpect( jsonPath( "$.items[*].version", hasItem( "1.0.0" ) ) )
         .andExpect( jsonPath( "$.items[*].name", hasItem( "Movement" ) ) )
         .andExpect( jsonPath( "$.items[*].type", hasItem( "BAMM" ) ) )
         .andExpect( jsonPath( "$.items[*].status", hasItem( "DRAFT" ) ) )
         .andExpect( jsonPath( "$.totalItems", greaterThan( 0 ) ) )
         .andExpect( jsonPath( "$.itemCount", greaterThan( 0 ) ) )
         .andExpect( status().isOk() );
   }

   @Test
   public void testSaveValidModelExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax:2.0.0#> .\\n \\n :TestAspect a bamm:Aspect;\\n bamm:name \\\"TestAspect\\\";\\n bamm:preferredName \\\"TestAspect\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"RELEASED\"}";

      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.urn", is( "urn:bamm:net.catenax:2.0.0#TestAspect" ) ) )
         .andExpect( jsonPath( "$.version", is( "2.0.0" ) ) )
         .andExpect( jsonPath( "$.name", is( "TestAspect" ) ) )
         .andExpect( jsonPath( "$.type", is( "BAMM" ) ) )
         .andExpect( jsonPath( "$.status", is( "RELEASED" ) ) )
         .andExpect( status().isOk() );
   }

   @Test
   public void testSaveInvalidModelExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax:1.0.0#> .\\n \\n :Movement a bamm:Aspect;\\n bamm:name \\\"Movement\\\";\\n bamm:preferredName \\\"Movement\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:propertiesX (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\"}";

      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.error.details.validationError1", containsString(
               "resultMessage:  Property needs to have at least 1 values, but found 0\nfocusNode:      urn:bamm:net.catenax:1.0.0#Movement\nresultPath:     urn:bamm:io.openmanufacturing:meta-model:1.0.0#properties\nresultSeverity: http://www.w3.org/ns/shacl#Violation\nvalue:          \n" ) ) )
         .andExpect( status().is4xxClientError() );
   }

   @Test
   public void testSaveExistingModelExpectBadRequest() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax:2.0.0#> .\\n \\n :TestAspect a bamm:Aspect;\\n bamm:name \\\"TestAspect\\\";\\n bamm:preferredName \\\"TestAspect\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\"}";

      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.error.message", containsString(
               "The package urn:bamm:net.catenax:2.0.0# is already in status RELEASE and cannot be modified." ) ) )
         .andExpect( status().isBadRequest() );
   }

   @Test
   public void testGenerateJsonSchemaExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax:1.0.0#> .\\n \\n :Movement a bamm:Aspect;\\n bamm:name \\\"Movement\\\";\\n bamm:preferredName \\\"Movement\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"DRAFT\"}";
      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get(
                     "/api/v1/models/{urn}/json-schema", "urn:bamm:net.catenax:1.0.0#Movement" )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( content().json(
               "{\"$schema\":\"http://json-schema.org/draft-04/schema\",\"type\":\"object\",\"components\":{\"schemas\":{\"urn_bamm_io.openmanufacturing_characteristic_1.0.0_Boolean\":{\"type\":\"boolean\"},\"urn_bamm_net.catenax_1.0.0_TrafficLight\":{\"type\":\"string\",\"enum\":[\"green\",\"yellow\",\"red\"]},\"urn_bamm_net.catenax_1.0.0_Coordinate\":{\"type\":\"number\"},\"urn_bamm_net.catenax_1.0.0_SpatialPositionCharacteristic\":{\"type\":\"object\",\"properties\":{\"x\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax_1.0.0_Coordinate\"},\"y\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax_1.0.0_Coordinate\"},\"z\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax_1.0.0_Coordinate\"}},\"required\":[\"x\",\"y\",\"z\"]}}},\"properties\":{\"isMoving\":{\"$ref\":\"#/components/schemas/urn_bamm_io.openmanufacturing_characteristic_1.0.0_Boolean\"},\"speedLimitWarning\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax_1.0.0_TrafficLight\"},\"position\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax_1.0.0_SpatialPositionCharacteristic\"}},\"required\":[\"isMoving\",\"speedLimitWarning\",\"position\"]}" ) )
         .andExpect( status().isOk() );
   }

   @Test
   public void testUpdateExistingModelInDraftStateExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax.testdraft:2.0.0#> .\\n \\n :TestAspectDraft a bamm:Aspect;\\n bamm:name \\\"TestAspectDraft\\\";\\n bamm:preferredName \\\"TestAspectDraft\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\":\"BAMM\", \"status\":\"DRAFT\"}";
      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.put( "/api/v1/models" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .contentType( MediaType.APPLICATION_JSON )
                                     .content( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( content().json(
               "{\"urn\":\"urn:bamm:net.catenax.testdraft:2.0.0#TestAspectDraft\",\"version\":\"2.0.0\",\"name\":\"TestAspectDraft\",\"type\":\"BAMM\"}" ) )
         .andExpect( status().isOk() );
   }

   @Test
   public void testUpdateExistingModelInReleasedStateExpectFailure() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax:3.0.0#> .\\n \\n :TestAspectDraft a bamm:Aspect;\\n bamm:name \\\"TestAspectDraft\\\";\\n bamm:preferredName \\\"TestAspectDraft\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"RELEASED\"}";
      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.put( "/api/v1/models" )
                                     .accept( MediaType.APPLICATION_JSON )
                                     .contentType( MediaType.APPLICATION_JSON )
                                     .content( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.error.message", containsString(
               "The package urn:bamm:net.catenax:3.0.0# is already in status RELEASE and cannot be modified." ) ) )
         .andExpect( status().isBadRequest() );
   }

   @Test
   public void testDeleteEndpointWithExistingModelPackageExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax.testdelete:3.0.0#> .\\n \\n :TestAspectDelete a bamm:Aspect;\\n bamm:name \\\"TestAspectDelete\\\";\\n bamm:preferredName \\\"TestAspectDelete\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"DRAFT\"}";
      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.delete(
                     "/api/v1/models/{urn}",
                     "urn:bamm:net.catenax.testdelete:3.0.0#" )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isNoContent() );
   }

   @Test
   public void testDeleteEndpointWithNotExistingModelExpectNotFound() throws Exception {
      mvc.perform(
               MockMvcRequestBuilders.delete(
                     "/api/v1/models/{urn}",
                     "urn:bamm:net.catenax.notexistingpackage:2.0.0#" )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isNotFound() );
   }

   @Test
   public void testGenerateOpenApiEndpointSpecExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax.testopenapi:1.0.0#> .\\n \\n :Movement a bamm:Aspect;\\n bamm:name \\\"Movement\\\";\\n bamm:preferredName \\\"Movement\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"DRAFT\"}";
      mvc.perform(
               post( insertModelJson )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models/{urn}/openapi?baseUrl=example.com",
                     "urn:bamm:net.catenax.testopenapi:1.0.0#Movement" ) )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() )
         .andExpect( content().json(
               "{\"openapi\":\"3.0.3\",\"info\":{\"title\":\"Movement\",\"version\":\"v1.0.0\"},\"servers\":[{\"url\":\"example.com/api/v1.0.0\",\"variables\":{\"api-version\":{\"default\":\"v1.0.0\"}}}],\"paths\":{\"/{tenant-id}/movement\":{\"get\":{\"tags\":[\"Movement\"],\"operationId\":\"getMovement\",\"parameters\":[{\"name\":\"tenant-id\",\"in\":\"path\",\"description\":\"The ID of the tenant owning the requested Twin.\",\"required\":true,\"schema\":{\"type\":\"string\",\"format\":\"uuid\"}}],\"responses\":{\"200\":{\"$ref\":\"#/components/responses/Movement\"},\"401\":{\"$ref\":\"#/components/responses/ClientError\"},\"402\":{\"$ref\":\"#/components/responses/Unauthorized\"},\"403\":{\"$ref\":\"#/components/responses/Forbidden\"},\"404\":{\"$ref\":\"#/components/responses/NotFoundError\"}}}}},\"components\":{\"schemas\":{\"ErrorResponse\":{\"type\":\"object\",\"required\":[\"error\"],\"properties\":{\"error\":{\"$ref\":\"#/components/schemas/Error\"}}},\"Error\":{\"type\":\"object\",\"required\":[\"details\"],\"properties\":{\"message\":{\"type\":\"string\",\"minLength\":1},\"path\":{\"type\":\"string\",\"minLength\":1},\"details\":{\"type\":\"object\",\"minLength\":1,\"additionalProperties\":{\"type\":\"object\"}},\"code\":{\"type\":\"string\",\"nullable\":true}}},\"urn_bamm_io.openmanufacturing_characteristic_1.0.0_Boolean\":{\"type\":\"boolean\"},\"urn_bamm_net.catenax.testopenapi_1.0.0_TrafficLight\":{\"type\":\"string\",\"enum\":[\"green\",\"yellow\",\"red\"]},\"urn_bamm_net.catenax.testopenapi_1.0.0_Coordinate\":{\"type\":\"number\"},\"urn_bamm_net.catenax.testopenapi_1.0.0_SpatialPositionCharacteristic\":{\"type\":\"object\",\"properties\":{\"x\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax.testopenapi_1.0.0_Coordinate\"},\"y\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax.testopenapi_1.0.0_Coordinate\"},\"z\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax.testopenapi_1.0.0_Coordinate\"}},\"required\":[\"x\",\"y\",\"z\"]},\"Movement\":{\"type\":\"object\",\"properties\":{\"isMoving\":{\"$ref\":\"#/components/schemas/urn_bamm_io.openmanufacturing_characteristic_1.0.0_Boolean\"},\"speedLimitWarning\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax.testopenapi_1.0.0_TrafficLight\"},\"position\":{\"$ref\":\"#/components/schemas/urn_bamm_net.catenax.testopenapi_1.0.0_SpatialPositionCharacteristic\"}},\"required\":[\"isMoving\",\"speedLimitWarning\",\"position\"]}},\"responses\":{\"Unauthorized\":{\"description\":\"The requesting user or client is not authenticated.\"},\"Forbidden\":{\"description\":\"The requesting user or client is not authorized to access resources for the given tenant.\"},\"NotFoundError\":{\"description\":\"The requested Twin has not been found.\"},\"ClientError\":{\"description\":\"Payload or user input is invalid. See error details in the payload for more.\",\"content\":{\"application/json\":{\"schema\":{\"$ref\":\"#/components/schemas/ErrorResponse\"}}}},\"Movement\":{\"content\":{\"application/json\":{\"schema\":{\"$ref\":\"#/components/schemas/Movement\"}}},\"description\":\"The request was successful.\"}},\"requestBodies\":{\"Movement\":{\"content\":{\"application/json\":{\"schema\":{\"$ref\":\"#/components/schemas/Movement\"}}}}}}}" ) )
      ;
   }

   @Test
   public void testExampleGenerateExamplePayloadJsonExpectSuccess() throws Exception {
      String insertModelJson = "{\"model\": \"@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .\\n @prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .\\n @prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .\\n @prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .\\n @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\\n @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\\n @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\\n @prefix : <urn:bamm:net.catenax.testjsonschema:1.0.0#> .\\n \\n :Movement a bamm:Aspect;\\n bamm:name \\\"Movement\\\";\\n bamm:preferredName \\\"Movement\\\"@en;\\n bamm:description \\\"Aspect for movement information\\\"@en;\\n bamm:properties (:isMoving :speedLimitWarning :position);\\n bamm:operations ().\\n :isMoving a bamm:Property;\\n bamm:name \\\"isMoving\\\";\\n bamm:preferredName \\\"Moving\\\"@en;\\n bamm:description \\\"Flag indicating whether the asset is currently moving\\\"@en;\\n bamm:characteristic bamm-c:Boolean.\\n :speedLimitWarning a bamm:Property;\\n bamm:name \\\"speedLimitWarning\\\";\\n bamm:preferredName \\\"Speed Limit Warning\\\"@en;\\n bamm:description \\\"Indicates if the speed limit is adhered to.\\\"@en;\\n bamm:characteristic :TrafficLight.\\n :position a bamm:Property;\\n bamm:name \\\"position\\\";\\n bamm:preferredName \\\"Position\\\"@en;\\n bamm:description \\\"Indicates a position\\\"@en;\\n bamm:characteristic :SpatialPositionCharacteristic.\\n :TrafficLight a bamm-c:Enumeration;\\n bamm:name \\\"TrafficLight\\\";\\n bamm:preferredName \\\"Warning Level\\\"@en;\\n bamm:description \\\"Represents if speed of position change is within specification (green), within tolerance (yellow), or outside specification (red).\\\"@en;\\n bamm:dataType xsd:string;\\n bamm-c:values (\\\"green\\\" \\\"yellow\\\" \\\"red\\\").\\n :SpatialPosition a bamm:Entity;\\n bamm:name \\\"SpatialPosition\\\";\\n bamm:preferredName \\\"Spatial Position\\\"@en;\\n bamm:description \\\"Position in space, described along three axis, with the third axis optional, if all positions are in a plane.\\\"@en;\\n bamm:properties (:x :y :z).\\n :x a bamm:Property;\\n bamm:name \\\"x\\\";\\n bamm:preferredName \\\"x\\\"@en;\\n bamm:description \\\"x coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :y a bamm:Property;\\n bamm:name \\\"y\\\";\\n bamm:preferredName \\\"y\\\"@en;\\n bamm:description \\\"y coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate.\\n :z a bamm:Property;\\n bamm:name \\\"z\\\";\\n bamm:preferredName \\\"z\\\"@en;\\n bamm:description \\\"z coordinate in space\\\"@en;\\n bamm:characteristic :Coordinate;\\n bamm:optional \\\"true\\\"^^xsd:boolean.\\n :Coordinate a bamm-c:Measurement;\\n bamm:name \\\"Coordinate\\\";\\n bamm:preferredName \\\"Coordinate\\\"@en;\\n bamm:description \\\"Represents a coordinate along an axis in space.\\\"@en;\\n bamm:dataType xsd:float;\\n bamm-c:unit unit:metre.\\n :SpatialPositionCharacteristic a bamm-c:SingleEntity;\\n bamm:name \\\"SpatialPositionCharacteristic\\\";\\n bamm:preferredName \\\"Spatial Position Characteristic\\\"@en;\\n bamm:description \\\"Represents a single position in space with optional z coordinate.\\\"@en;\\n bamm:dataType :SpatialPosition.\\n\",\"type\": \"BAMM\", \"status\":\"DRAFT\"}";
      mvc.perform( post( insertModelJson ) )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models/{urn}/example-payload",
                     "urn:bamm:net.catenax.testjsonschema:1.0.0#Movement" )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.isMoving" ).exists() )
         .andExpect( jsonPath( "$.speedLimitWarning" ).exists() )
         .andExpect( jsonPath( "$.position.x" ).exists() )
         .andExpect( jsonPath( "$.position.y" ).exists() )
         .andExpect( jsonPath( "$.position.z" ).exists() )
         .andExpect( status().isOk() );
   }

   /**
    * This test verifies that existing triples e.g. characteristic can be referenced.
    */
   @Test
   public void testSaveModelWithExternalReferencesExpectSuccess() throws Exception {
      // save the model with external reference to a traceability characteristic
      // this will fail because traceability does not exist yet
      String modelWithReferenceToTraceability = TestUtils.loadModelFromResources(
            TestUtils.MODEL_WITH_REFERENCE_TO_TRACEABILITY_MODEL_PATH );
      mvc.perform( post( TestUtils.createNewModelRequestJson( modelWithReferenceToTraceability, "DRAFT" ) ) )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isBadRequest() )
         .andExpect( jsonPath( "$.error.message", is( "Validation failed." ) ) )
         .andExpect( jsonPath( "$.error.details.validationError1",
               containsString( "urn:bamm:com.catenax.traceability:0.1.1#PartStaticDataCharacteristic" ) ) );

      // save the traceability aspect model
      String traceability = TestUtils.loadModelFromResources(
            TestUtils.TRACEABILITY_MODEL_PATH );
      mvc.perform( post( TestUtils.createNewModelRequestJson( traceability, "DRAFT" ) ) )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      // save again the model with external reference and validate the result
      mvc.perform( post( TestUtils.createNewModelRequestJson( modelWithReferenceToTraceability, "DRAFT" ) ) )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() );

      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models/{urn}/example-payload",
                     "urn:bamm:com.catenaX.modelwithreferencetotraceability:0.1.1#ModelWithReferenceToTraceability" )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( jsonPath( "$.staticData" ).exists() )
         .andExpect( jsonPath( "$.staticData.manufacturerOneID" ).exists() )
         .andExpect( jsonPath( "$.staticData.partNumberManufacturer" ).exists() )
         .andExpect( jsonPath( "$.staticData.customerOneID" ).exists() )
         .andExpect( jsonPath( "$.staticData.partNameManufacturer" ).exists() )
         .andExpect( jsonPath( "$.staticData.customerContractOneID" ).exists() )
         .andExpect( jsonPath( "$.staticData.partNameCustomer" ).exists() )
         .andExpect( jsonPath( "$.staticData.manufactureContractOneID" ).exists() )
         .andExpect( jsonPath( "$.staticData.partNumberCustomer" ).exists() )
         .andExpect( status().isOk() );

      // verify that the turtle file contains a complete resolved model
      String traceabilityBaseUrn = "urn:bamm:com.catenax.traceability:0.1.1";
      String modelExtBaseUrn = "urn:bamm:com.catenaX.modelwithreferencetotraceability:0.1.1";
      mvc.perform(
               MockMvcRequestBuilders.get( "/api/v1/models/{urn}/file",
                     "urn:bamm:com.catenaX.modelwithreferencetotraceability:0.1.1#ModelWithReferenceToTraceability" )
         )
         .andDo( MockMvcResultHandlers.print() )
         .andExpect( status().isOk() )
         .andExpect( content().string( containsString( modelExtBaseUrn + "#ModelWithReferenceToTraceability" ) ) )
         .andExpect( content().string( containsString( modelExtBaseUrn + "#staticData" ) ) )
         .andExpect( content().string( containsString( traceabilityBaseUrn + "#OneIDBusinessPartner" ) ) )
         .andExpect( content().string( containsString( traceabilityBaseUrn + "#partNameCustomer" ) ) )
         .andExpect( content().string( containsString( traceabilityBaseUrn + "#partNumberCustomer" ) ) )
         .andExpect( content().string( containsString( traceabilityBaseUrn + "#customerOneID" ) ) )
         .andExpect( content().string( containsString( traceabilityBaseUrn + "#manufacturerOneID" ) ) )
         .andExpect( content().string( containsString( traceabilityBaseUrn + "#PartStaticDataCharacteristic" ) ) );
   }

   private MockHttpServletRequestBuilder post( String payload ) {
      return MockMvcRequestBuilders.post( "/api/v1/models" )
                                   .accept( MediaType.APPLICATION_JSON )
                                   .contentType( MediaType.APPLICATION_JSON )
                                   .content( payload );
   }
}