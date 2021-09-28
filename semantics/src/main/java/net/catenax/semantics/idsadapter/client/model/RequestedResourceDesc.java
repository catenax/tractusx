/*
 * Dataspace Connector
 * IDS Connector originally developed by the Fraunhofer ISST
 *
 * OpenAPI spec version: 6.2.0
 * Contact: info@dataspace-connector.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package net.catenax.semantics.idsadapter.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * RequestedResourceDesc
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")
public class RequestedResourceDesc {
  @JsonProperty("title")
  private String title = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("keywords")
  private List<String> keywords = null;

  @JsonProperty("publisher")
  private String publisher = null;

  @JsonProperty("language")
  private String language = null;

  @JsonProperty("license")
  private String license = null;

  @JsonProperty("sovereign")
  private String sovereign = null;

  @JsonProperty("endpointDocumentation")
  private String endpointDocumentation = null;

  @JsonProperty("samples")
  private List<String> samples = null;

  @JsonProperty("remoteId")
  private String remoteId = null;

  /**
   * Gets or Sets paymentMethod
   */
  public enum PaymentMethodEnum {
    UNDEFINED("undefined"),
    FIXEDPRICE("fixedPrice"),
    FREE("free"),
    NEGOTIATIONBASIS("negotiationBasis");

    private String value;

    PaymentMethodEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static PaymentMethodEnum fromValue(String text) {
      for (PaymentMethodEnum b : PaymentMethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("paymentMethod")
  private PaymentMethodEnum paymentMethod = null;

  public RequestedResourceDesc title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @Schema(description = "")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public RequestedResourceDesc description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @Schema(description = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public RequestedResourceDesc keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public RequestedResourceDesc addKeywordsItem(String keywordsItem) {
    if (this.keywords == null) {
      this.keywords = new ArrayList<>();
    }
    this.keywords.add(keywordsItem);
    return this;
  }

   /**
   * Get keywords
   * @return keywords
  **/
  @Schema(description = "")
  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public RequestedResourceDesc publisher(String publisher) {
    this.publisher = publisher;
    return this;
  }

   /**
   * Get publisher
   * @return publisher
  **/
  @Schema(description = "")
  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public RequestedResourceDesc language(String language) {
    this.language = language;
    return this;
  }

   /**
   * Get language
   * @return language
  **/
  @Schema(description = "")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public RequestedResourceDesc license(String license) {
    this.license = license;
    return this;
  }

   /**
   * Get license
   * @return license
  **/
  @Schema(description = "")
  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public RequestedResourceDesc sovereign(String sovereign) {
    this.sovereign = sovereign;
    return this;
  }

   /**
   * Get sovereign
   * @return sovereign
  **/
  @Schema(description = "")
  public String getSovereign() {
    return sovereign;
  }

  public void setSovereign(String sovereign) {
    this.sovereign = sovereign;
  }

  public RequestedResourceDesc endpointDocumentation(String endpointDocumentation) {
    this.endpointDocumentation = endpointDocumentation;
    return this;
  }

   /**
   * Get endpointDocumentation
   * @return endpointDocumentation
  **/
  @Schema(description = "")
  public String getEndpointDocumentation() {
    return endpointDocumentation;
  }

  public void setEndpointDocumentation(String endpointDocumentation) {
    this.endpointDocumentation = endpointDocumentation;
  }

  public RequestedResourceDesc samples(List<String> samples) {
    this.samples = samples;
    return this;
  }

  public RequestedResourceDesc addSamplesItem(String samplesItem) {
    if (this.samples == null) {
      this.samples = new ArrayList<>();
    }
    this.samples.add(samplesItem);
    return this;
  }

   /**
   * Get samples
   * @return samples
  **/
  @Schema(description = "")
  public List<String> getSamples() {
    return samples;
  }

  public void setSamples(List<String> samples) {
    this.samples = samples;
  }

   /**
   * Get remoteId
   * @return remoteId
  **/
  @Schema(description = "")
  public String getRemoteId() {
    return remoteId;
  }

  public RequestedResourceDesc paymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

   /**
   * Get paymentMethod
   * @return paymentMethod
  **/
  @Schema(description = "")
  public PaymentMethodEnum getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestedResourceDesc requestedResourceDesc = (RequestedResourceDesc) o;
    return Objects.equals(this.title, requestedResourceDesc.title) &&
        Objects.equals(this.description, requestedResourceDesc.description) &&
        Objects.equals(this.keywords, requestedResourceDesc.keywords) &&
        Objects.equals(this.publisher, requestedResourceDesc.publisher) &&
        Objects.equals(this.language, requestedResourceDesc.language) &&
        Objects.equals(this.license, requestedResourceDesc.license) &&
        Objects.equals(this.sovereign, requestedResourceDesc.sovereign) &&
        Objects.equals(this.endpointDocumentation, requestedResourceDesc.endpointDocumentation) &&
        Objects.equals(this.samples, requestedResourceDesc.samples) &&
        Objects.equals(this.remoteId, requestedResourceDesc.remoteId) &&
        Objects.equals(this.paymentMethod, requestedResourceDesc.paymentMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, keywords, publisher, language, license, sovereign, endpointDocumentation, samples, remoteId, paymentMethod);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestedResourceDesc {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    publisher: ").append(toIndentedString(publisher)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    license: ").append(toIndentedString(license)).append("\n");
    sb.append("    sovereign: ").append(toIndentedString(sovereign)).append("\n");
    sb.append("    endpointDocumentation: ").append(toIndentedString(endpointDocumentation)).append("\n");
    sb.append("    samples: ").append(toIndentedString(samples)).append("\n");
    sb.append("    remoteId: ").append(toIndentedString(remoteId)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
