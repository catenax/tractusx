package net.catenax.semantics.framework.dsc.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import feign.Response;
import net.catenax.semantics.framework.dsc.client.model.ArtifactDesc;
import net.catenax.semantics.framework.dsc.client.model.PagedModelRepresentationView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelSubscriptionView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.ArtifactView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelAgreementView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelArtifactView;
import net.catenax.semantics.framework.dsc.client.model.StreamingResponseBody;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface ArtifactsApi extends ApiClient.Api {

  /**
   * Add a list of children to a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   * @return PagedModelRepresentationView
   */
  @RequestLine("POST /api/artifacts/{id}/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView addResources14(List<String> body, @Param("id") UUID id);
  /**
   * Create a base resource
   * 
   * @param body  (required)
   * @return ArtifactView
   */
  @RequestLine("POST /api/artifacts")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  ArtifactView create11(ArtifactDesc body);
  /**
   * Delete a base resource by id
   * 
   * @param id  (required)
   */
  @RequestLine("DELETE /api/artifacts/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void delete12(@Param("id") UUID id);
  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return ArtifactView
   */
  @RequestLine("GET /api/artifacts/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  ArtifactView get12(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelArtifactView
   */
  @RequestLine("GET /api/artifacts?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getAll12(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll12</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll12QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelArtifactView

   */
  @RequestLine("GET /api/artifacts?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getAll12(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll12</code> method in a fluent style.
   */
  public static class GetAll12QueryParams extends HashMap<String, Object> {
    public GetAll12QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll12QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Get data by artifact id with query input
   * 
   * @param id  (required)
   * @return StreamingResponseBody
   */
  @RequestLine("POST /api/artifacts/{id}/data")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*",
  })
  Response getData(@Param("id") UUID id);
  /**
   * Get data by artifact id with query input
   * 
   * @param id  (required)
   * @param download  (optional)
   * @param agreementUri  (optional)
   * @return StreamingResponseBody
   */
  @RequestLine("GET /api/artifacts/{id}/data/**?download={download}&agreementUri={agreementUri}")
  @Headers({
      "Accept: */*,*/*",
  })
  StreamingResponseBody getData1(@Param("id") UUID id, @Param("download") Boolean download, @Param("agreementUri") String agreementUri);

  /**
   * Get data by artifact id with query input
   * 
   * Note, this is equivalent to the other <code>getData1</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetData1QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>download -  (optional)</li>
   *   <li>agreementUri -  (optional)</li>
   *   </ul>
   * @return StreamingResponseBody

   */
  @RequestLine("GET /api/artifacts/{id}/data/**?download={download}&agreementUri={agreementUri}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  StreamingResponseBody getData1(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getData1</code> method in a fluent style.
   */
  public static class GetData1QueryParams extends HashMap<String, Object> {
    public GetData1QueryParams download(final Boolean value) {
      put("download", EncodingUtils.encode(value));
      return this;
    }
    public GetData1QueryParams agreementUri(final String value) {
      put("agreementUri", EncodingUtils.encode(value));
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
  @RequestLine("GET /api/artifacts/{id}/subscriptions?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getResource21(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource21</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource21QueryParams} class that allows for
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
  @RequestLine("GET /api/artifacts/{id}/subscriptions?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelSubscriptionView getResource21(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource21</code> method in a fluent style.
   */
  public static class GetResource21QueryParams extends HashMap<String, Object> {
    public GetResource21QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource21QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/artifacts/{id}/representations?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView getResource22(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource22</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource22QueryParams} class that allows for
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
  @RequestLine("GET /api/artifacts/{id}/representations?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelRepresentationView getResource22(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource22</code> method in a fluent style.
   */
  public static class GetResource22QueryParams extends HashMap<String, Object> {
    public GetResource22QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource22QueryParams size(final Integer value) {
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
   * @return PagedModelAgreementView
   */
  @RequestLine("GET /api/artifacts/{id}/agreements?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelAgreementView getResource23(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource23</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource23QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param id  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelAgreementView

   */
  @RequestLine("GET /api/artifacts/{id}/agreements?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelAgreementView getResource23(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource23</code> method in a fluent style.
   */
  public static class GetResource23QueryParams extends HashMap<String, Object> {
    public GetResource23QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource23QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * 
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/artifacts/{id}/data")
  @Headers({
      "Content-Type: */*",
      "Accept: */*",
  })
  void putData(List<byte[]> body, @Param("id") UUID id);
  /**
   * Remove a list of children from a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/artifacts/{id}/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void removeResources14(List<String> body, @Param("id") UUID id);
  /**
   * Replace the children of a base resource
   * 
   * @param body  (required)
   * @param id  (required)
   */
  @RequestLine("PUT /api/artifacts/{id}/representations")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*",
  })
  void replaceResources14(List<String> body, @Param("id") UUID id);
  /**
   * Update a base resource by id
   * 
   * @param body  (required)
   * @param id  (required)
   * @return ArtifactView
   */
  @RequestLine("PUT /api/artifacts/{id}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  ArtifactView update12(ArtifactDesc body, @Param("id") UUID id);
}
