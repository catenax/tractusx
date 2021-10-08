package com.catenax.tdm.api.v1.client;

import com.catenax.tdm.api.v1.ApiClient;

import io.swagger.client.model.BusinessPartner;
import io.swagger.client.model.Error;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2021-10-03T20:34:34.146648200+02:00[Europe/Berlin]")
@Component("com.catenax.tdm.api.v1.client.BusinessPartnerServiceApi")

public class BusinessPartnerServiceApi {
    private ApiClient apiClient;

    public BusinessPartnerServiceApi() {
        this(new ApiClient());
    }

    @Autowired
    public BusinessPartnerServiceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Create Business Partner BPN
     * Create Business Partner BPN
     * <p><b>200</b> - Business Partner BPN
     * <p><b>0</b> - Unexpected error
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public String createBusinessPartnerNumber() throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/businesspartner/bpn").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
    /**
     * Get Business Partner
     * Get Business Partner
     * <p><b>200</b> - Business Partner BPN
     * <p><b>0</b> - Unexpected error
     * @param businessPartnerNumber ID of Business Partner
     * @return List&lt;BusinessPartner&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public List<BusinessPartner> getBusinessPartner(String businessPartnerNumber) throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/businesspartner").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "BusinessPartnerNumber", businessPartnerNumber));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<List<BusinessPartner>> returnType = new ParameterizedTypeReference<List<BusinessPartner>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
}

