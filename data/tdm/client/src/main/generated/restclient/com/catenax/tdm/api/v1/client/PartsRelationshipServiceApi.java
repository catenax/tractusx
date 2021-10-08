package com.catenax.tdm.api.v1.client;

import com.catenax.tdm.api.v1.ApiClient;

import io.swagger.client.model.ErrorResponse;
import io.swagger.client.model.PartRelationshipWithInfos;


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
@Component("com.catenax.tdm.api.v1.client.PartsRelationshipServiceApi")

public class PartsRelationshipServiceApi {
    private ApiClient apiClient;

    public PartsRelationshipServiceApi() {
        this(new ApiClient());
    }

    @Autowired
    public PartsRelationshipServiceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Create new vehicle BOM
     * 
     * <p><b>200</b> - Created BOM
     * <p><b>404</b> - BOM not found
     * @param oneid OneID of manufacturer
     * @param count number of vehicles to create
     * @param vehicleType Vehicle Type
     * @return List&lt;PartRelationshipWithInfos&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public List<PartRelationshipWithInfos> createVehicle(String oneid, Integer count, String vehicleType) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'oneid' is set
        if (oneid == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oneid' when calling createVehicle");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oneid", oneid);
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/vehicle/create/{oneid}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "count", count));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "vehicleType", vehicleType));

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<List<PartRelationshipWithInfos>> returnType = new ParameterizedTypeReference<List<PartRelationshipWithInfos>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
}

