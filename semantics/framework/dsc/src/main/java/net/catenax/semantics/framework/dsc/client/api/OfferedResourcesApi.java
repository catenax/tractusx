package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.OfferedResourceDesc;
import net.catenax.semantics.framework.dsc.client.model.OfferedResourceView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelBrokerView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelCatalogView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelContractView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelRepresentationView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelSubscriptionView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface OfferedResourcesApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelCatalogView
   */
  @RequestLine("POST /api/offers/{id}/catalogs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelCatalogView addResourcesCatalog(List<String> body, @Param("id") UUID id);
  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelRepresentationView
   */
  @RequestLine("POST /api/offers/{id}/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView addResourcesRepresentation(List<String> body, @Param("id") UUID id);
  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelContractView
   */
  @RequestLine("POST /api/offers/{id}/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelContractView addResourcesContracts(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return OfferedResourceView
   */
  @RequestLine("POST /api/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  OfferedResourceView create4(OfferedResourceDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/offers/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete5(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return OfferedResourceView
   */
  @RequestLine("GET /api/offers/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  OfferedResourceView get5(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelOfferedResourceView
   */
  @RequestLine("GET /api/offers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getAll5(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll5</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll5QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelOfferedResourceView

   */
  @RequestLine("GET /api/offers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getAll5(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll5</code> method in a fluent style.
   */
  public static class GetAll5QueryParams extends HashMap<String, Object> {
    public GetAll5QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll5QueryParams size(final Integer value) {
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
   * @return PagedModelSubscriptionView
   */
  @RequestLine("GET /api/offers/{id}/subscriptions?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getResource11(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource11</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource11QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelSubscriptionView

   */
  @RequestLine("GET /api/offers/{id}/subscriptions?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getResource11(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource11</code> method in a fluent style.
   */
  public static class GetResource11QueryParams extends HashMap<String, Object> {
    public GetResource11QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource11QueryParams size(final Integer value) {
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
   * @return PagedModelRepresentationView
   */
  @RequestLine("GET /api/offers/{id}/representations?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView getResource12(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource12</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource12QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelRepresentationView

   */
  @RequestLine("GET /api/offers/{id}/representations?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView getResource12(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource12</code> method in a fluent style.
   */
  public static class GetResource12QueryParams extends HashMap<String, Object> {
    public GetResource12QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource12QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/offers/{id}/contracts?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelContractView getResource13(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource13</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource13QueryParams} class that allows for
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
  @RequestLine("GET /api/offers/{id}/contracts?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelContractView getResource13(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource13</code> method in a fluent style.
   */
  public static class GetResource13QueryParams extends HashMap<String, Object> {
    public GetResource13QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource13QueryParams size(final Integer value) {
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
   * @return PagedModelCatalogView
   */
  @RequestLine("GET /api/offers/{id}/catalogs?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelCatalogView getResource14(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource14</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource14QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelCatalogView

   */
  @RequestLine("GET /api/offers/{id}/catalogs?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelCatalogView getResource14(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource14</code> method in a fluent style.
   */
  public static class GetResource14QueryParams extends HashMap<String, Object> {
    public GetResource14QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource14QueryParams size(final Integer value) {
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
   * @return PagedModelBrokerView
   */
  @RequestLine("GET /api/offers/{id}/brokers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelBrokerView getResource15(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource15</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource15QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelBrokerView

   */
  @RequestLine("GET /api/offers/{id}/brokers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelBrokerView getResource15(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource15</code> method in a fluent style.
   */
  public static class GetResource15QueryParams extends HashMap<String, Object> {
    public GetResource15QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource15QueryParams size(final Integer value) {
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
  @RequestLine("DELETE /api/offers/{id}/catalogs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources10(List<String> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/offers/{id}/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources8(List<String> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/offers/{id}/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources9(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/offers/{id}/catalogs")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources10(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/offers/{id}/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources8(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/offers/{id}/contracts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources9(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return OfferedResourceView
   */
  @RequestLine("PUT /api/offers/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  OfferedResourceView update5(OfferedResourceDesc body, @Param("id") UUID id);
}
