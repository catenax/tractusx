package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.BrokerDesc;
import net.catenax.semantics.framework.dsc.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.BrokerView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelBrokerView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface BrokersApi extends ApiClient.Api {

  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return BrokerView
   */
  @RequestLine("POST /api/brokers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  BrokerView create10(BrokerDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/brokers/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete11(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return BrokerView
   */
  @RequestLine("GET /api/brokers/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  BrokerView get11(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelBrokerView
   */
  @RequestLine("GET /api/brokers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelBrokerView getAll11(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll11</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll11QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelBrokerView

   */
  @RequestLine("GET /api/brokers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelBrokerView getAll11(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll11</code> method in a fluent style.
   */
  public static class GetAll11QueryParams extends HashMap<String, Object> {
    public GetAll11QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll11QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/brokers/{id}/offers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource20(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource20</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource20QueryParams} class that allows for
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
  @RequestLine("GET /api/brokers/{id}/offers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource20(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource20</code> method in a fluent style.
   */
  public static class GetResource20QueryParams extends HashMap<String, Object> {
    public GetResource20QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource20QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return BrokerView
   */
  @RequestLine("PUT /api/brokers/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  BrokerView update11(BrokerDesc body, @Param("id") UUID id);
}
