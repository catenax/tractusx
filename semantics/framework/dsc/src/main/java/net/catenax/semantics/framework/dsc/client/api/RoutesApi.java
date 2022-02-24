package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.RouteView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.PagedModelArtifactView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelRouteView;
import net.catenax.semantics.framework.dsc.client.model.RouteDesc;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface RoutesApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelRouteView
   */
  @RequestLine("POST /api/routes/{id}/steps")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelRouteView addResources1(List<String> body, @Param("id") UUID id);
  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelArtifactView
   */
  @RequestLine("POST /api/routes/{id}/outputs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelArtifactView addResources2(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return RouteView
   */
  @RequestLine("POST /api/routes")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  RouteView create2(RouteDesc body);
  /**
   * Creates last endpoint for the route
   * 
   * @param body  (required)
   * @param id  (required)
   * @return String
   */
  @RequestLine("PUT /api/routes/{id}/endpoint/end")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  String createLastEndpoint(UUID body, @Param("id") UUID id);
  /**
   * Creates start endpoint for the route
   * 
   * @param body  (required)
   * @param id  (required)
   * @return String
   */
  @RequestLine("PUT /api/routes/{id}/endpoint/start")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  String createStartEndpoint(UUID body, @Param("id") UUID id);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/routes/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete2(@Param("id") UUID id);
  /**
   * Deletes the start endpoint of the route
   * 
   * @param id  (required)
   * @return String
   */
  @RequestLine("DELETE /api/routes/{id}/endpoint/end")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  String deleteLastEndpoint(@Param("id") UUID id);
  /**
   * Deletes the start endpoint of the route
   * 
   * @param id  (required)
   * @return String
   */
  @RequestLine("DELETE /api/routes/{id}/endpoint/start")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  String deleteStartEndpoint(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return RouteView
   */
  @RequestLine("GET /api/routes/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  RouteView get2(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelRouteView
   */
  @RequestLine("GET /api/routes?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRouteView getAll2(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll2</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll2QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelRouteView

   */
  @RequestLine("GET /api/routes?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRouteView getAll2(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll2</code> method in a fluent style.
   */
  public static class GetAll2QueryParams extends HashMap<String, Object> {
    public GetAll2QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll2QueryParams size(final Integer value) {
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
   * @return PagedModelRouteView
   */
  @RequestLine("GET /api/routes/{id}/steps?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRouteView getResource1(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource1</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource1QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelRouteView

   */
  @RequestLine("GET /api/routes/{id}/steps?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRouteView getResource1(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource1</code> method in a fluent style.
   */
  public static class GetResource1QueryParams extends HashMap<String, Object> {
    public GetResource1QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource1QueryParams size(final Integer value) {
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
   * @return PagedModelArtifactView
   */
  @RequestLine("GET /api/routes/{id}/outputs?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getResource2(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource2</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource2QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelArtifactView

   */
  @RequestLine("GET /api/routes/{id}/outputs?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getResource2(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource2</code> method in a fluent style.
   */
  public static class GetResource2QueryParams extends HashMap<String, Object> {
    public GetResource2QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource2QueryParams size(final Integer value) {
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
  @RequestLine("DELETE /api/routes/{id}/steps")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources1(List<String> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/routes/{id}/outputs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources2(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/routes/{id}/steps")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources1(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/routes/{id}/outputs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources2(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return RouteView
   */
  @RequestLine("PUT /api/routes/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  RouteView update2(RouteDesc body, @Param("id") UUID id);
}
