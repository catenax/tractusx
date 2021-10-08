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
import io.swagger.client.model.MemberCompanyRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

/**
 * Member Company
 */@Schema(description = "Member Company")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2021-10-03T20:34:34.146648200+02:00[Europe/Berlin]")
public class MemberCompany {

  
  @JsonProperty("BPN")
  private String BPN = null;
  
  
  @JsonProperty("name")
  private String name = null;
  
  
  @JsonProperty("parent")
  private String parent = null;
  
  
  @JsonProperty("roles")
  private List<MemberCompanyRole> roles = null;
  
  public MemberCompany BPN(String BPN) {
    this.BPN = BPN;
    return this;
  }

  
  /**
  * Get BPN
  * @return BPN
  **/
  
  
  @Schema(required = true, description = "")
  public String getBPN() {
    return BPN;
  }
  public void setBPN(String BPN) {
    this.BPN = BPN;
  }
  
  public MemberCompany name(String name) {
    this.name = name;
    return this;
  }

  
  /**
  * Get name
  * @return name
  **/
  
  
  @Schema(required = true, description = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  public MemberCompany parent(String parent) {
    this.parent = parent;
    return this;
  }

  
  /**
  * Get parent
  * @return parent
  **/
  
  
  @Schema(description = "")
  public String getParent() {
    return parent;
  }
  public void setParent(String parent) {
    this.parent = parent;
  }
  
  public MemberCompany roles(List<MemberCompanyRole> roles) {
    this.roles = roles;
    return this;
  }

  public MemberCompany addRolesItem(MemberCompanyRole rolesItem) {
    
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    
    this.roles.add(rolesItem);
    return this;
  }
  
  /**
  * Get roles
  * @return roles
  **/
  
  
  @Schema(description = "")
  public List<MemberCompanyRole> getRoles() {
    return roles;
  }
  public void setRoles(List<MemberCompanyRole> roles) {
    this.roles = roles;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MemberCompany memberCompany = (MemberCompany) o;
    return Objects.equals(this.BPN, memberCompany.BPN) &&
        Objects.equals(this.name, memberCompany.name) &&
        Objects.equals(this.parent, memberCompany.parent) &&
        Objects.equals(this.roles, memberCompany.roles);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(BPN, name, parent, roles);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MemberCompany {\n");
    
    sb.append("    BPN: ").append(toIndentedString(BPN)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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



