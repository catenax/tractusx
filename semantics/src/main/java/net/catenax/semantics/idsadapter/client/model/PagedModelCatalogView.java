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
import net.catenax.semantics.idsadapter.client.model.Links;
import net.catenax.semantics.idsadapter.client.model.PageMetadata;
import net.catenax.semantics.idsadapter.client.model.PagedModelCatalogViewEmbedded;
/**
 * PagedModelCatalogView
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")
public class PagedModelCatalogView {
  @JsonProperty("_embedded")
  private PagedModelCatalogViewEmbedded _embedded = null;

  @JsonProperty("_links")
  private Links _links = null;

  @JsonProperty("page")
  private PageMetadata page = null;

  public PagedModelCatalogView _embedded(PagedModelCatalogViewEmbedded _embedded) {
    this._embedded = _embedded;
    return this;
  }

   /**
   * Get _embedded
   * @return _embedded
  **/
  @Schema(description = "")
  public PagedModelCatalogViewEmbedded getEmbedded() {
    return _embedded;
  }

  public void setEmbedded(PagedModelCatalogViewEmbedded _embedded) {
    this._embedded = _embedded;
  }

  public PagedModelCatalogView _links(Links _links) {
    this._links = _links;
    return this;
  }

   /**
   * Get _links
   * @return _links
  **/
  @Schema(description = "")
  public Links getLinks() {
    return _links;
  }

  public void setLinks(Links _links) {
    this._links = _links;
  }

  public PagedModelCatalogView page(PageMetadata page) {
    this.page = page;
    return this;
  }

   /**
   * Get page
   * @return page
  **/
  @Schema(description = "")
  public PageMetadata getPage() {
    return page;
  }

  public void setPage(PageMetadata page) {
    this.page = page;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagedModelCatalogView pagedModelCatalogView = (PagedModelCatalogView) o;
    return Objects.equals(this._embedded, pagedModelCatalogView._embedded) &&
        Objects.equals(this._links, pagedModelCatalogView._links) &&
        Objects.equals(this.page, pagedModelCatalogView.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_embedded, _links, page);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagedModelCatalogView {\n");
    
    sb.append("    _embedded: ").append(toIndentedString(_embedded)).append("\n");
    sb.append("    _links: ").append(toIndentedString(_links)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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
