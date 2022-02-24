package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.PagedModelSubscriptionView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.SubscriptionDesc;
import net.catenax.semantics.framework.dsc.client.model.SubscriptionView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface SubscriptionsApi extends ApiClient.Api {

  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return SubscriptionView
   */
  @RequestLine("POST /api/subscriptions")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  SubscriptionView create(SubscriptionDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/subscriptions/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return SubscriptionView
   */
  @RequestLine("GET /api/subscriptions/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  SubscriptionView get(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelSubscriptionView
   */
  @RequestLine("GET /api/subscriptions?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getAll(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelSubscriptionView

   */
  @RequestLine("GET /api/subscriptions?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getAll(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll</code> method in a fluent style.
   */
  public static class GetAllQueryParams extends HashMap<String, Object> {
    public GetAllQueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllQueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * 
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   */
  @RequestLine("GET /api/subscriptions/owning?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  void getAllFiltered(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * 
   * 
   * Note, this is equivalent to the other <code>getAllFiltered</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllFilteredQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>

   */
  @RequestLine("GET /api/subscriptions/owning?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  void getAllFiltered(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAllFiltered</code> method in a fluent style.
   */
  public static class GetAllFilteredQueryParams extends HashMap<String, Object> {
    public GetAllFilteredQueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAllFilteredQueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return SubscriptionView
   */
  @RequestLine("PUT /api/subscriptions/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  SubscriptionView update(SubscriptionDesc body, @Param("id") UUID id);
}
