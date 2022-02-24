package net.catenax.semantics.framework.dsc.client.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;
import net.catenax.semantics.framework.dsc.client.invoker.EncodingUtils;
import net.catenax.semantics.framework.dsc.client.model.SubscriptionDesc;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface MessagesApi extends ApiClient.Api {

  /**
   * Notify all subscribers
   * Can be used to manually notify all subscribers about a resource offer, representation, or artifact update.
   * @param elementId The element id. (required)
   * @return Object
   */
  @RequestLine("PUT /api/notify?elementId={elementId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*",
  })
  Object sendMessage(@Param("elementId") String elementId);

  /**
   * Notify all subscribers
   * Can be used to manually notify all subscribers about a resource offer, representation, or artifact update.
   * Note, this is equivalent to the other <code>sendMessage</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessageQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>elementId - The element id. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("PUT /api/notify?elementId={elementId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*",
  })
  Object sendMessage(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage</code> method in a fluent style.
   */
  public static class SendMessageQueryParams extends HashMap<String, Object> {
    public SendMessageQueryParams elementId(final String value) {
      put("elementId", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Resource update message
   * Can be used for registering or updating a resource at an IDS broker or consumer connector.
   * @param recipient The recipient url. (required)
   * @param resourceId The resource id. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/resource/update?recipient={recipient}&resourceId={resourceId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage1(@Param("recipient") String recipient, @Param("resourceId") String resourceId);

  /**
   * Resource update message
   * Can be used for registering or updating a resource at an IDS broker or consumer connector.
   * Note, this is equivalent to the other <code>sendMessage1</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage1QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>resourceId - The resource id. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/resource/update?recipient={recipient}&resourceId={resourceId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage1(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage1</code> method in a fluent style.
   */
  public static class SendMessage1QueryParams extends HashMap<String, Object> {
    public SendMessage1QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public SendMessage1QueryParams resourceId(final String value) {
      put("resourceId", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Resource unavailable message
   * Can be used for e.g. unregistering a resource at an IDS broker.
   * @param recipient The recipient url. (required)
   * @param resourceId The resource id. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/resource/unavailable?recipient={recipient}&resourceId={resourceId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage2(@Param("recipient") String recipient, @Param("resourceId") String resourceId);

  /**
   * Resource unavailable message
   * Can be used for e.g. unregistering a resource at an IDS broker.
   * Note, this is equivalent to the other <code>sendMessage2</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage2QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>resourceId - The resource id. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/resource/unavailable?recipient={recipient}&resourceId={resourceId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage2(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage2</code> method in a fluent style.
   */
  public static class SendMessage2QueryParams extends HashMap<String, Object> {
    public SendMessage2QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public SendMessage2QueryParams resourceId(final String value) {
      put("resourceId", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Send IDS description request message
   * 
   * @param recipient The recipient url. (required)
   * @param elementId The id of the requested resource. (optional)
   * @return Object
   */
  @RequestLine("POST /api/ids/description?recipient={recipient}&elementId={elementId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*",
  })
  Object sendDescriptionRequest(@Param("recipient") String recipient, @Param("elementId") String elementId);

  /**
   * Send IDS description request message
   * 
   * Note, this is equivalent to the other <code>sendMessage3</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage3QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>elementId - The id of the requested resource. (optional)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/description?recipient={recipient}&elementId={elementId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage3(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage3</code> method in a fluent style.
   */
  public static class SendMessage3QueryParams extends HashMap<String, Object> {
    public SendMessage3QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public SendMessage3QueryParams elementId(final String value) {
      put("elementId", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Send IDS contract request message
   * 
   * @param body  (required)
   * @param recipient The recipient url. (required)
   * @param resourceIds List of ids resource that should be requested. (required)
   * @param artifactIds List of ids artifacts that should be requested. (required)
   * @param download Indicates whether the connector should automatically download data of an artifact. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/contract?recipient={recipient}&resourceIds={resourceIds}&artifactIds={artifactIds}&download={download}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendContract(List<Object> body, @Param("recipient") String recipient, @Param("resourceIds") List<String> resourceIds, @Param("artifactIds") List<String> artifactIds, @Param("download") Boolean download);

  /**
   * Send IDS contract request message
   * 
   * Note, this is equivalent to the other <code>sendMessage4</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage4QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param body  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>resourceIds - List of ids resource that should be requested. (required)</li>
   *   <li>artifactIds - List of ids artifacts that should be requested. (required)</li>
   *   <li>download - Indicates whether the connector should automatically download data of an artifact. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/contract?recipient={recipient}&resourceIds={resourceIds}&artifactIds={artifactIds}&download={download}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage4(List<Object> body, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage4</code> method in a fluent style.
   */
  public static class SendMessage4QueryParams extends HashMap<String, Object> {
    public SendMessage4QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public SendMessage4QueryParams resourceIds(final List<String> value) {
      put("resourceIds", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
    public SendMessage4QueryParams artifactIds(final List<String> value) {
      put("artifactIds", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
    public SendMessage4QueryParams download(final Boolean value) {
      put("download", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Connector update message
   * Can be used for registering or updating the connector at an IDS broker.
   * @param recipient The recipient url. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/connector/update?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage5(@Param("recipient") String recipient);

  /**
   * Connector update message
   * Can be used for registering or updating the connector at an IDS broker.
   * Note, this is equivalent to the other <code>sendMessage5</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage5QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/connector/update?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage5(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage5</code> method in a fluent style.
   */
  public static class SendMessage5QueryParams extends HashMap<String, Object> {
    public SendMessage5QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Connector unavailable message
   * Can be used for unregistering the connector at an IDS broker.
   * @param recipient The recipient url. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/connector/unavailable?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage6(@Param("recipient") String recipient);

  /**
   * Connector unavailable message
   * Can be used for unregistering the connector at an IDS broker.
   * Note, this is equivalent to the other <code>sendMessage6</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage6QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/connector/unavailable?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage6(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage6</code> method in a fluent style.
   */
  public static class SendMessage6QueryParams extends HashMap<String, Object> {
    public SendMessage6QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Download IDS app from AppStore
   * 
   * @param recipient The recipient url. (required)
   * @param appId The app id. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/app?recipient={recipient}&appId={appId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage7(@Param("recipient") String recipient, @Param("appId") String appId);

  /**
   * Download IDS app from AppStore
   * 
   * Note, this is equivalent to the other <code>sendMessage7</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendMessage7QueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>appId - The app id. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/app?recipient={recipient}&appId={appId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendMessage7(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendMessage7</code> method in a fluent style.
   */
  public static class SendMessage7QueryParams extends HashMap<String, Object> {
    public SendMessage7QueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public SendMessage7QueryParams appId(final String value) {
      put("appId", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Query message
   * Can be used for querying an IDS component (e.g. the Broker).
   * @param body  (required)
   * @param recipient The recipient url. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/query?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendQueryMessage(String body, @Param("recipient") String recipient);

  /**
   * Query message
   * Can be used for querying an IDS component (e.g. the Broker).
   * Note, this is equivalent to the other <code>sendQueryMessage</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendQueryMessageQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param body  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/query?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*,*/*",
  })
  Object sendQueryMessage(String body, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendQueryMessage</code> method in a fluent style.
   */
  public static class SendQueryMessageQueryParams extends HashMap<String, Object> {
    public SendQueryMessageQueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Full text search
   * Can be used for full text search at an IDS component (e.g. the Broker).
   * @param body  (required)
   * @param recipient The recipient url. (required)
   * @param limit The limit value. (required)
   * @param offset The offset value. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/search?recipient={recipient}&limit={limit}&offset={offset}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*",
  })
  Object sendSearchMessage(String body, @Param("recipient") String recipient, @Param("limit") Integer limit, @Param("offset") Integer offset);

  /**
   * Full text search
   * Can be used for full text search at an IDS component (e.g. the Broker).
   * Note, this is equivalent to the other <code>sendSearchMessage</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SendSearchMessageQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param body  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>limit - The limit value. (required)</li>
   *   <li>offset - The offset value. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/search?recipient={recipient}&limit={limit}&offset={offset}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*",
  })
  Object sendSearchMessage(String body, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>sendSearchMessage</code> method in a fluent style.
   */
  public static class SendSearchMessageQueryParams extends HashMap<String, Object> {
    public SendSearchMessageQueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public SendSearchMessageQueryParams limit(final Integer value) {
      put("limit", EncodingUtils.encode(value));
      return this;
    }
    public SendSearchMessageQueryParams offset(final Integer value) {
      put("offset", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Send IDS request message for element subscription
   * 
   * @param body  (required)
   * @param recipient The recipient url. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/subscribe?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*",
  })
  Object subscribe(SubscriptionDesc body, @Param("recipient") String recipient);

  /**
   * Send IDS request message for element subscription
   * 
   * Note, this is equivalent to the other <code>subscribe</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link SubscribeQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param body  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/subscribe?recipient={recipient}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*,*/*",
  })
  Object subscribe(SubscriptionDesc body, @QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>subscribe</code> method in a fluent style.
   */
  public static class SubscribeQueryParams extends HashMap<String, Object> {
    public SubscribeQueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
  }
  /**
   * Send IDS request message for element unsubscription
   * 
   * @param recipient The recipient url. (required)
   * @param elementId The subscription object. (required)
   * @return Object
   */
  @RequestLine("POST /api/ids/unsubscribe?recipient={recipient}&elementId={elementId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*",
  })
  Object unsubscribe(@Param("recipient") String recipient, @Param("elementId") String elementId);

  /**
   * Send IDS request message for element unsubscription
   * 
   * Note, this is equivalent to the other <code>unsubscribe</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link UnsubscribeQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>recipient - The recipient url. (required)</li>
   *   <li>elementId - The subscription object. (required)</li>
   *   </ul>
   * @return Object

   */
  @RequestLine("POST /api/ids/unsubscribe?recipient={recipient}&elementId={elementId}")
  @Headers({
      "Content-Type: application/json",
      "Accept: */*,*/*,*/*,*/*",
  })
  Object unsubscribe(@QueryMap(encoded=true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>unsubscribe</code> method in a fluent style.
   */
  public static class UnsubscribeQueryParams extends HashMap<String, Object> {
    public UnsubscribeQueryParams recipient(final String value) {
      put("recipient", EncodingUtils.encode(value));
      return this;
    }
    public UnsubscribeQueryParams elementId(final String value) {
      put("elementId", EncodingUtils.encode(value));
      return this;
    }
  }
}
