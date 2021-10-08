package com.catenax.tdm.api.v1.client;

import com.catenax.tdm.api.v1.ApiClient;

import java.time.LocalDate;
import io.swagger.client.model.PartAspectUpdate;
import io.swagger.client.model.PartRelationshipUpdateList;
import io.swagger.client.model.PartTypeNameUpdate;


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
@Component("com.catenax.tdm.api.v1.client.PartsRelationshipServiceBrokerHttpProxyApiApi")

public class PartsRelationshipServiceBrokerHttpProxyApiApi {
    private ApiClient apiClient;

    public PartsRelationshipServiceBrokerHttpProxyApiApi() {
        this(new ApiClient());
    }

    @Autowired
    public PartsRelationshipServiceBrokerHttpProxyApiApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.
     * 
     * <p><b>200</b> - Found the BOM
     * @param bpn The bpn parameter
     * @param effectiveDateTimeStart The effectiveDateTimeStart parameter
     * @param effectiveDateTimeEnd The effectiveDateTimeEnd parameter
     * @return List&lt;PartAspectUpdate&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public List<PartAspectUpdate> getPartAspectUpdate(String bpn, LocalDate effectiveDateTimeStart, LocalDate effectiveDateTimeEnd) throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/prs/broker/PartAspectUpdate").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "bpn", bpn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "effectiveDateTimeStart", effectiveDateTimeStart));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "effectiveDateTimeEnd", effectiveDateTimeEnd));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<List<PartAspectUpdate>> returnType = new ParameterizedTypeReference<List<PartAspectUpdate>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
    /**
     * Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.
     * 
     * <p><b>200</b> - Found the BOM
     * @param bpn The bpn parameter
     * @param effectiveDateTimeStart The effectiveDateTimeStart parameter
     * @param effectiveDateTimeEnd The effectiveDateTimeEnd parameter
     * @return List&lt;PartRelationshipUpdateList&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public List<PartRelationshipUpdateList> getPartRelationshipUpdateList(String bpn, LocalDate effectiveDateTimeStart, LocalDate effectiveDateTimeEnd) throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/prs/broker/PartRelationshipUpdateList").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "bpn", bpn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "effectiveDateTimeStart", effectiveDateTimeStart));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "effectiveDateTimeEnd", effectiveDateTimeEnd));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<List<PartRelationshipUpdateList>> returnType = new ParameterizedTypeReference<List<PartRelationshipUpdateList>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
    /**
     * Get a PartTypeNameUpdate. Describes an update of a part type name.
     * 
     * <p><b>200</b> - Found the BOM
     * @param bpn The bpn parameter
     * @param effectiveDateTimeStart The effectiveDateTimeStart parameter
     * @param effectiveDateTimeEnd The effectiveDateTimeEnd parameter
     * @return List&lt;PartTypeNameUpdate&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public List<PartTypeNameUpdate> getPartTypeNameUpdate(String bpn, LocalDate effectiveDateTimeStart, LocalDate effectiveDateTimeEnd) throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/prs/broker/PartTypeNameUpdate").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "bpn", bpn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "effectiveDateTimeStart", effectiveDateTimeStart));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "effectiveDateTimeEnd", effectiveDateTimeEnd));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<List<PartTypeNameUpdate>> returnType = new ParameterizedTypeReference<List<PartTypeNameUpdate>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
}

