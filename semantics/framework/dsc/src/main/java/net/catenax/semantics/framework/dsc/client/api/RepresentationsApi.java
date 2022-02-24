package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.PagedModelArtifactView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelRepresentationView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelRequestedResourceView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelSubscriptionView;
import net.catenax.semantics.framework.dsc.client.model.RepresentationDesc;
import net.catenax.semantics.framework.dsc.client.model.RepresentationView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface RepresentationsApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelRequestedResourceView
   */
  @RequestLine("POST /api/representations/{id}/requests")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelRequestedResourceView addResourcesRequest(List<String> body, @Param("id") UUID id);
  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelOfferedResourceView
   */
  @RequestLine("POST /api/representations/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView addResourcesOffer(List<String> body, @Param("id") UUID id);
  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelArtifactView
   */
  @RequestLine("POST /api/representations/{id}/artifacts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelArtifactView addResourcesArtifact(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return RepresentationView
   */
  @RequestLine("POST /api/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  RepresentationView create3(RepresentationDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/representations/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete4(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return RepresentationView
   */
  @RequestLine("GET /api/representations/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  RepresentationView get4(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelRepresentationView
   */
  @RequestLine("GET /api/representations?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView getAll4(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll4</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll4QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelRepresentationView

   */
  @RequestLine("GET /api/representations?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView getAll4(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll4</code> method in a fluent style.
   */
  public static class GetAll4QueryParams extends HashMap<String, Object> {
    public GetAll4QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll4QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/representations/{id}/artifacts?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getResource10(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource10</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource10QueryParams} class that allows for
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
  @RequestLine("GET /api/representations/{id}/artifacts?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getResource10(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource10</code> method in a fluent style.
   */
  public static class GetResource10QueryParams extends HashMap<String, Object> {
    public GetResource10QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource10QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/representations/{id}/subscriptions?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getResource7(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource7</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource7QueryParams} class that allows for
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
  @RequestLine("GET /api/representations/{id}/subscriptions?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getResource7(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource7</code> method in a fluent style.
   */
  public static class GetResource7QueryParams extends HashMap<String, Object> {
    public GetResource7QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource7QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/representations/{id}/requests?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRequestedResourceView getResource8(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource8</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource8QueryParams} class that allows for
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
  @RequestLine("GET /api/representations/{id}/requests?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRequestedResourceView getResource8(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource8</code> method in a fluent style.
   */
  public static class GetResource8QueryParams extends HashMap<String, Object> {
    public GetResource8QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource8QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/representations/{id}/offers?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource9(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource9</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource9QueryParams} class that allows for
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
  @RequestLine("GET /api/representations/{id}/offers?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelOfferedResourceView getResource9(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource9</code> method in a fluent style.
   */
  public static class GetResource9QueryParams extends HashMap<String, Object> {
    public GetResource9QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource9QueryParams size(final Integer value) {
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
  @RequestLine("DELETE /api/representations/{id}/requests")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources5(List<String> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/representations/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources6(List<String> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/representations/{id}/artifacts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources7(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/representations/{id}/requests")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources5(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/representations/{id}/offers")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources6(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/representations/{id}/artifacts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources7(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return RepresentationView
   */
  @RequestLine("PUT /api/representations/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  RepresentationView update4(RepresentationDesc body, @Param("id") UUID id);
}
