package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.CatalogDesc;
import net.catenax.semantics.framework.dsc.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.CatalogView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelCatalogView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface CatalogsApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelOfferedResourceView
   */
  @RequestLine("POST /api/catalogs/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView addResourcesOffer(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return CatalogView
   */
  @RequestLine("POST /api/catalogs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  CatalogView create9(CatalogDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/catalogs/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete10(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return CatalogView
   */
  @RequestLine("GET /api/catalogs/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  CatalogView get10(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelCatalogView
   */
  @RequestLine("GET /api/catalogs?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelCatalogView getAll10(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll10</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll10QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelCatalogView

   */
  @RequestLine("GET /api/catalogs?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelCatalogView getAll10(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll10</code> method in a fluent style.
   */
  public static class GetAll10QueryParams extends HashMap<String, Object> {
    public GetAll10QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll10QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Get all children of a base resource with pagination
   * 
   * @param id  (required)
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelOfferedResourceView
   */
  @RequestLine("GET /api/catalogs/{id}/offers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource19(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource19</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource19QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelOfferedResourceView

   */
  @RequestLine("GET /api/catalogs/{id}/offers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource19(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource19</code> method in a fluent style.
   */
  public static class GetResource19QueryParams extends HashMap<String, Object> {
    public GetResource19QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource19QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/catalogs/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources13(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/catalogs/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources13(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return CatalogView
   */
  @RequestLine("PUT /api/catalogs/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  CatalogView update10(CatalogDesc body, @Param("id") UUID id);
}
