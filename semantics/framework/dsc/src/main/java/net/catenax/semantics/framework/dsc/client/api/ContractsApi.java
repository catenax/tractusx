package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.ContractDesc;
import net.catenax.semantics.framework.dsc.client.model.PagedModelContractView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelRequestedResourceView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.ContractView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelContractRuleView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface ContractsApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelContractRuleView
   */
  @RequestLine("POST /api/contracts/{id}/rules")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelContractRuleView addResourcesRule(List<String> body, @Param("id") UUID id);
  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelOfferedResourceView
   */
  @RequestLine("POST /api/contracts/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView addResourcesOffers(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return ContractView
   */
  @RequestLine("POST /api/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  ContractView create7(ContractDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/contracts/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete8(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return ContractView
   */
  @RequestLine("GET /api/contracts/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  ContractView get8(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelContractView
   */
  @RequestLine("GET /api/contracts?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelContractView getAll8(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll8</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll8QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelContractView

   */
  @RequestLine("GET /api/contracts?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelContractView getAll8(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll8</code> method in a fluent style.
   */
  public static class GetAll8QueryParams extends HashMap<String, Object> {
    public GetAll8QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll8QueryParams size(final Integer value) {
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
   * @return PagedModelContractRuleView
   */
  @RequestLine("GET /api/contracts/{id}/rules?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelContractRuleView getResource16(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource16</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource16QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelContractRuleView

   */
  @RequestLine("GET /api/contracts/{id}/rules?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelContractRuleView getResource16(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource16</code> method in a fluent style.
   */
  public static class GetResource16QueryParams extends HashMap<String, Object> {
    public GetResource16QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource16QueryParams size(final Integer value) {
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
   * @return PagedModelRequestedResourceView
   */
  @RequestLine("GET /api/contracts/{id}/requests?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRequestedResourceView getResource17(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource17</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource17QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelRequestedResourceView

   */
  @RequestLine("GET /api/contracts/{id}/requests?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRequestedResourceView getResource17(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource17</code> method in a fluent style.
   */
  public static class GetResource17QueryParams extends HashMap<String, Object> {
    public GetResource17QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource17QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/contracts/{id}/offers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource18(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource18</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource18QueryParams} class that allows for
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
  @RequestLine("GET /api/contracts/{id}/offers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource18(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource18</code> method in a fluent style.
   */
  public static class GetResource18QueryParams extends HashMap<String, Object> {
    public GetResource18QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource18QueryParams size(final Integer value) {
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
  @RequestLine("DELETE /api/contracts/{id}/rules")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources11(List<String> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/contracts/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources12(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/contracts/{id}/rules")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources11(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/contracts/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources12(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return ContractView
   */
  @RequestLine("PUT /api/contracts/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  ContractView update8(ContractDesc body, @Param("id") UUID id);
}
