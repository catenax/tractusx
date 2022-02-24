package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.ContractRuleDesc;
import net.catenax.semantics.framework.dsc.client.model.ContractRuleView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelContractView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.PagedModelContractRuleView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface RulesApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelContractView
   */
  @RequestLine("POST /api/rules/{id}/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelContractView addResources(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return ContractRuleView
   */
  @RequestLine("POST /api/rules")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  ContractRuleView create1(ContractRuleDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/rules/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete1(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return ContractRuleView
   */
  @RequestLine("GET /api/rules/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  ContractRuleView get1(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelContractRuleView
   */
  @RequestLine("GET /api/rules?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelContractRuleView getAll1(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll1</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll1QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelContractRuleView

   */
  @RequestLine("GET /api/rules?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelContractRuleView getAll1(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll1</code> method in a fluent style.
   */
  public static class GetAll1QueryParams extends HashMap<String, Object> {
    public GetAll1QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll1QueryParams size(final Integer value) {
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
   * @return PagedModelContractView
   */
  @RequestLine("GET /api/rules/{id}/contracts?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelContractView getResource(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResourceQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelContractView

   */
  @RequestLine("GET /api/rules/{id}/contracts?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelContractView getResource(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource</code> method in a fluent style.
   */
  public static class GetResourceQueryParams extends HashMap<String, Object> {
    public GetResourceQueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResourceQueryParams size(final Integer value) {
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
  @RequestLine("DELETE /api/rules/{id}/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/rules/{id}/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return ContractRuleView
   */
  @RequestLine("PUT /api/rules/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  ContractRuleView update1(ContractRuleDesc body, @Param("id") UUID id);
}
