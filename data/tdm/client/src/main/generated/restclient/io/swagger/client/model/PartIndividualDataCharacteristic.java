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

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PartIndividualDataCharacteristic
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2021-10-03T20:34:34.146648200+02:00[Europe/Berlin]")
public class PartIndividualDataCharacteristic {

  
  @JsonProperty("productionCountryCode")
  private String productionCountryCode = null;
  
  
  @JsonProperty("productionDateGMT")
  private String productionDateGMT = null;
  
  public PartIndividualDataCharacteristic productionCountryCode(String productionCountryCode) {
    this.productionCountryCode = productionCountryCode;
    return this;
  }

  
  /**
  * Get productionCountryCode
  * @return productionCountryCode
  **/
  
  
  @Schema(required = true, description = "")
  public String getProductionCountryCode() {
    return productionCountryCode;
  }
  public void setProductionCountryCode(String productionCountryCode) {
    this.productionCountryCode = productionCountryCode;
  }
  
  public PartIndividualDataCharacteristic productionDateGMT(String productionDateGMT) {
    this.productionDateGMT = productionDateGMT;
    return this;
  }

  
  /**
  * Instant at which the update was applied
  * @return productionDateGMT
  **/
  
  
  @Schema(required = true, description = "Instant at which the update was applied")
  public String getProductionDateGMT() {
    return productionDateGMT;
  }
  public void setProductionDateGMT(String productionDateGMT) {
    this.productionDateGMT = productionDateGMT;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartIndividualDataCharacteristic partIndividualDataCharacteristic = (PartIndividualDataCharacteristic) o;
    return Objects.equals(this.productionCountryCode, partIndividualDataCharacteristic.productionCountryCode) &&
        Objects.equals(this.productionDateGMT, partIndividualDataCharacteristic.productionDateGMT);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(productionCountryCode, productionDateGMT);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartIndividualDataCharacteristic {\n");
    
    sb.append("    productionCountryCode: ").append(toIndentedString(productionCountryCode)).append("\n");
    sb.append("    productionDateGMT: ").append(toIndentedString(productionDateGMT)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  
}



