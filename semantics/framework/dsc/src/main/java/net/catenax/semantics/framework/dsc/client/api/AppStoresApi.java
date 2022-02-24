package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.AppStoreDesc;
import net.catenax.semantics.framework.dsc.client.model.AppStoreView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelAppStoreView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.PagedModelAppView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface AppStoresApi extends ApiClient.Api {

  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return AppStoreView
   */
  @RequestLine("POST /api/appstores")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  AppStoreView create12(AppStoreDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/appstores/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete13(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return AppStoreView
   */
  @RequestLine("GET /api/appstores/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  AppStoreView get13(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelAppStoreView
   */
  @RequestLine("GET /api/appstores?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelAppStoreView getAll13(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll13</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll13QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelAppStoreView

   */
  @RequestLine("GET /api/appstores?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelAppStoreView getAll13(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll13</code> method in a fluent style.
   */
  public static class GetAll13QueryParams extends HashMap<String, Object> {
    public GetAll13QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll13QueryParams size(final Integer value) {
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
   * @return PagedModelAppView
   */
  @RequestLine("GET /api/appstores/{id}/apps?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelAppView getResource24(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource24</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource24QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelAppView

   */
  @RequestLine("GET /api/appstores/{id}/apps?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelAppView getResource24(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource24</code> method in a fluent style.
   */
  public static class GetResource24QueryParams extends HashMap<String, Object> {
    public GetResource24QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource24QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return AppStoreView
   */
  @RequestLine("PUT /api/appstores/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  AppStoreView update13(AppStoreDesc body, @Param("id") UUID id);
}
