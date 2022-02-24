package net.catenax.semantics.framework.dsc.client.api;

import feign.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")public interface ConnectorApi extends ApiClient.Api {

  /**
   * Private IDS self-description
   * 
   * @return Object
   */
  @RequestLine("GET /api/connector")
  @Headers({
      "Accept: application/ld+json,application/ld+json,application/ld+json",
  })
  Object getPrivateSelfDescription();
  /**
   * Public IDS self-description
   * 
   * @return Object
   */
  @RequestLine("GET /")
  @Headers({
      "Accept: application/ld+json,application/ld+json",
  })
  Object getPublicSelfDescription1();
}
