package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.PagedModelDataSourceView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.DataSourceDesc;
import net.catenax.semantics.framework.dsc.client.model.DataSourceView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface DataSourcesSinksApi extends ApiClient.Api {

  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return DataSourceView
   */
  @RequestLine("POST /api/datasources")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  DataSourceView create6(DataSourceDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/datasources/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete7(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return DataSourceView
   */
  @RequestLine("GET /api/datasources/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  DataSourceView get7(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelDataSourceView
   */
  @RequestLine("GET /api/datasources?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelDataSourceView getAll7(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll7</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll7QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelDataSourceView

   */
  @RequestLine("GET /api/datasources?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelDataSourceView getAll7(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll7</code> method in a fluent style.
   */
  public static class GetAll7QueryParams extends HashMap<String, Object> {
    public GetAll7QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll7QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return DataSourceView
   */
  @RequestLine("PUT /api/datasources/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  DataSourceView update7(DataSourceDesc body, @Param("id") UUID id);
}
