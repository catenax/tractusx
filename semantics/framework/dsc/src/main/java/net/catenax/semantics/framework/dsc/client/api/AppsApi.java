package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.AppView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelAppEndpointView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelAppView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface AppsApi extends ApiClient.Api {

  /**
   * Actions on apps
   * Can be used for managing apps.
   * @param id  (required)
   * @param actionType  (required)
   * @return Object
   */
  @RequestLine("PUT /api/apps/{id}/actions?actionType={actionType}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*",
  })
  Object containerManagement(@Param("id") UUID id, @Param("actionType") String actionType);

  /**
   * Actions on apps
   * Can be used for managing apps.
   * Note, this is equivalent to the other <code>containerManagement</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link ContainerManagementQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>actionType -  (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("PUT /api/apps/{id}/actions?actionType={actionType}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*",
  })
  Object containerManagement(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>containerManagement</code> method in a fluent style.
   */
  public static class ContainerManagementQueryParams extends HashMap<String, Object> {
    public ContainerManagementQueryParams actionType(final String value) {
      put("actionType", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/apps/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete14(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return AppView
   */
  @RequestLine("GET /api/apps/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  AppView get14(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelAppView
   */
  @RequestLine("GET /api/apps?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelAppView getAll14(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll14</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll14QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelAppView

   */
  @RequestLine("GET /api/apps?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelAppView getAll14(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll14</code> method in a fluent style.
   */
  public static class GetAll14QueryParams extends HashMap<String, Object> {
    public GetAll14QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll14QueryParams size(final Integer value) {
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
   * @return PagedModelAppEndpointView
   */
  @RequestLine("GET /api/apps/{id}/endpoints?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelAppEndpointView getResource25(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource25</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource25QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelAppEndpointView

   */
  @RequestLine("GET /api/apps/{id}/endpoints?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelAppEndpointView getResource25(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource25</code> method in a fluent style.
   */
  public static class GetResource25QueryParams extends HashMap<String, Object> {
    public GetResource25QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource25QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Get appstore by app id
   * Get appstore holding this app.
   * @param id  (required)
   * @return Object
   */
  @RequestLine("GET /api/apps/{id}/appstore")
  @Headers({
      "Accept: */*,*/*,*/*,*/*",
  })
  Object relatedAppStore(@Param("id") UUID id);
}
