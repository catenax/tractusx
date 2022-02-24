package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.ApiEndpointsBody;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.EndpointsIdBody;
import net.catenax.semantics.framework.dsc.client.model.PagedModelObject;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface EndpointsApi extends ApiClient.Api {

  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return Object
   */
  @RequestLine("POST /api/endpoints")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  Object create5(ApiEndpointsBody body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/endpoints/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete6(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return Object
   */
  @RequestLine("GET /api/endpoints/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  Object get6(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelObject
   */
  @RequestLine("GET /api/endpoints?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelObject getAll6(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll6</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll6QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelObject

   */
  @RequestLine("GET /api/endpoints?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelObject getAll6(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll6</code> method in a fluent style.
   */
  public static class GetAll6QueryParams extends HashMap<String, Object> {
    public GetAll6QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll6QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Creates start endpoint for the route
   * 
   * @param id  (required)
   * @param dataSourceId  (required)
   */
  @RequestLine("PUT /api/endpoints/{id}/datasource/{dataSourceId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void linkDataSource(@Param("id") UUID id, @Param("dataSourceId") UUID dataSourceId);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return Object
   */
  @RequestLine("PUT /api/endpoints/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  Object update6(EndpointsIdBody body, @Param("id") UUID id);
}
