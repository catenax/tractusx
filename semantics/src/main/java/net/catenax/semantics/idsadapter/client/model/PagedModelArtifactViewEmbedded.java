/*
 * Dataspace Connector
 * IDS Connector originally developed by the Fraunhofer ISST
 *
 * OpenAPI spec version: 6.2.0
 * Contact: info@dataspace-connector.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package net.catenax.semantics.idsadapter.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import net.catenax.semantics.idsadapter.client.model.ArtifactView;

import java.util.ArrayList;
import java.util.List;
/**
 * PagedModelArtifactViewEmbedded
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")
public class PagedModelArtifactViewEmbedded {
  @JsonProperty("artifacts")
  private List<ArtifactView> artifacts = null;

  public PagedModelArtifactViewEmbedded artifacts(List<ArtifactView> artifacts) {
    this.artifacts = artifacts;
    return this;
  }

  public PagedModelArtifactViewEmbedded addArtifactsItem(ArtifactView artifactsItem) {
    if (this.artifacts == null) {
      this.artifacts = new ArrayList<>();
    }
    this.artifacts.add(artifactsItem);
    return this;
  }

   /**
   * Get artifacts
   * @return artifacts
  **/
  @Schema(description = "")
  public List<ArtifactView> getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(List<ArtifactView> artifacts) {
    this.artifacts = artifacts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagedModelArtifactViewEmbedded pagedModelArtifactViewEmbedded = (PagedModelArtifactViewEmbedded) o;
    return Objects.equals(this.artifacts, pagedModelArtifactViewEmbedded.artifacts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(artifacts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagedModelArtifactViewEmbedded {\n");
    
    sb.append("    artifacts: ").append(toIndentedString(artifacts)).append("\n");
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
