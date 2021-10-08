/*
 * Catena-X Speedboat Test Data Generator
 * Disclaimer: This service serves synthetic, none-productive data for testing purposes only. All BOMs, part trees, VINs, serialNos etc. are synthetic
 *
 * OpenAPI spec version: 0.0.1 Speedboat
 * Contact: christian.kabelin@partner.bmw.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;



/**
 * Gets or Sets DisassemblyStatus
 */

public enum DisassemblyStatus {
  
  ASSEMBLED("assembled"),
  
  DISASSEMBLED("disassembled");

  private String value;

  DisassemblyStatus(String value) {
    this.value = value;
  }


  @JsonValue

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }


  @JsonCreator

  public static DisassemblyStatus fromValue(String text) {
    for (DisassemblyStatus b : DisassemblyStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

}



