package net.catenax.semantics.framework.dsc.client.api;

import java.util.HashMap;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.ExamplesPolicyBody;
import net.catenax.semantics.framework.dsc.client.model.InlineResponse401;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface UsageControlApi extends ApiClient.Api {

  /**
   * Get example policy
   * Get an example policy for a given policy pattern.
   * @param body  (required)
   * @return Object
   */
  @RequestLine("POST /api/examples/policy")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  Object getExampleUsagePolicy(ExamplesPolicyBody body);
  /**
   * Get contract negotiation status
   * 
   * @return InlineResponse401
   */
  @RequestLine("GET /api/configuration/negotiation")
  @Headers({
      "Accept: application/json",
  })
  InlineResponse401 getNegotiationStatus();
  /**
   * Get pattern validation status
   * Return if unsupported patterns are ignored when requesting data.
   * @return InlineResponse401
   */
  @RequestLine("GET /api/configuration/pattern")
  @Headers({
      "Accept: application/json",
  })
  InlineResponse401 getPatternStatus();
  /**
   * Get pattern of policy
   * Get the policy pattern represented by a given JSON string.
   * @param body  (required)
   * @return Object
   */
  @RequestLine("POST /api/examples/validation")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*",
  })
  Object getPolicyPattern(String body);
  /**
   * Set contract negotiation status
   * 
   * @param status  (required)
   * @return InlineResponse401
   */
  @RequestLine("PUT /api/configuration/negotiation?status={status}")
  @Headers({
      "Content-Type: application/json",
      "Accept: application/json",
  })
  InlineResponse401 setNegotiationStatus(@Param("status") Boolean status);

  /**
   * Set contract negotiation status
   * 
   * Note, this is equivalent to the other <code>setNegotiationStatus</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SetNegotiationStatusQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>status -  (required)</li>
   *   </ul>
   * @return InlineResponse401

   */
  @RequestLine("PUT /api/configuration/negotiation?status={status}")
  @Headers({
      "Content-Type: application/json",
      "Accept: application/json",
  })
  InlineResponse401 setNegotiationStatus(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>setNegotiationStatus</code> method in a fluent style.
   */
  public static class SetNegotiationStatusQueryParams extends HashMap<String, Object> {
    public SetNegotiationStatusQueryParams status(final Boolean value) {
      put("status", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Allow unsupported patterns
   * Allow requesting data without policy enforcement if an unsupported pattern is recognized.
   * @param status  (required)
   * @return InlineResponse401
   */
  @RequestLine("PUT /api/configuration/pattern?status={status}")
  @Headers({
      "Content-Type: application/json",
      "Accept: application/json",
  })
  InlineResponse401 setPatternStatus(@Param("status") Boolean status);

  /**
   * Allow unsupported patterns
   * Allow requesting data without policy enforcement if an unsupported pattern is recognized.
   * Note, this is equivalent to the other <code>setPatternStatus</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SetPatternStatusQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>status -  (required)</li>
   *   </ul>
   * @return InlineResponse401

   */
  @RequestLine("PUT /api/configuration/pattern?status={status}")
  @Headers({
      "Content-Type: application/json",
      "Accept: application/json",
  })
  InlineResponse401 setPatternStatus(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>setPatternStatus</code> method in a fluent style.
   */
  public static class SetPatternStatusQueryParams extends HashMap<String, Object> {
    public SetPatternStatusQueryParams status(final Boolean value) {
      put("status", EncodingUtils.encode(value));
      return this;
    }
  }
}
