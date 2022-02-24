package net.catenax.semantics.framework.dsc.client.api;

import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.model.AgreementView;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.PagedModelAgreementView;
import net.catenax.semantics.framework.dsc.client.model.PagedModelArtifactView;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface AgreementsApi extends ApiClient.Api {

  /**
   * Get a base resource by id
   * 
   * @param id  (required)
   * @return AgreementView
   */
  @RequestLine("GET /api/agreements/{id}")
  @Headers({
      "Accept: */*,*/*",
  })
  AgreementView get15(@Param("id") UUID id);
  /**
   * Get a list of base resources with pagination
   * 
   * @param page  (optional, default to 0)
   * @param size  (optional, default to 30)
   * @return PagedModelAgreementView
   */
  @RequestLine("GET /api/agreements?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelAgreementView getAll15(@Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get a list of base resources with pagination
   * 
   * Note, this is equivalent to the other <code>getAll15</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAll15QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>page -  (optional, default to 0)</li>
   *   <li>size -  (optional, default to 30)</li>
   *   </ul>
   * @return PagedModelAgreementView

   */
  @RequestLine("GET /api/agreements?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelAgreementView getAll15(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getAll15</code> method in a fluent style.
   */
  public static class GetAll15QueryParams extends HashMap<String, Object> {
    public GetAll15QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetAll15QueryParams size(final Integer value) {
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
  @RequestLine("GET /api/agreements/{id}/artifacts?page={page}&size={size}")
  @Headers({
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getArtifacts(@Param("id") UUID id, @Param("page") Integer page, @Param("size") Integer size);

  /**
   * Get all children of a base resource with pagination
   * 
   * Note, this is equivalent to the other <code>getResource26</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetResource26QueryParams} class that allows for
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
  @RequestLine("GET /api/agreements/{id}/artifacts?page={page}&size={size}")
  @Headers({
      "Content-Type: */*",
      "Accept: */*,*/*",
  })
  PagedModelArtifactView getArtifactsQueryParams(@Param("id") UUID id, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>getResource26</code> method in a fluent style.
   */
  public static class GetResource26QueryParams extends HashMap<String, Object> {
    public GetResource26QueryParams page(final Integer value) {
      put("page", EncodingUtils.encode(value));
      return this;
    }
    public GetResource26QueryParams size(final Integer value) {
      put("size", EncodingUtils.encode(value));
      return this;
    }
  }
}
