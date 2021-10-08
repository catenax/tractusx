package com.catenax.tdm.api.v1.client;

import com.catenax.tdm.api.v1.ApiClient;

import io.swagger.client.model.ErrorResponse;


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
@Component("com.catenax.tdm.api.v1.client.AspectModelApi")

public class AspectModelApi {
    private ApiClient apiClient;

    public AspectModelApi() {
        this(new ApiClient());
    }

    @Autowired
    public AspectModelApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Retrieve Aspect
     * 
     * <p><b>200</b> - Created BOM
     * <p><b>404</b> - Aspect not found
     * @param aspect Aspect Name
     * @param oneID Business Partner OneID
     * @param partUniqueID UniqueID of part
     * @return List&lt;Object&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public List<Object> getAspect(String aspect, String oneID, String partUniqueID) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'aspect' is set
        if (aspect == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'aspect' when calling getAspect");
        }
        
        // verify the required parameter 'oneID' is set
        if (oneID == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oneID' when calling getAspect");
        }
        
        // verify the required parameter 'partUniqueID' is set
        if (partUniqueID == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'partUniqueID' when calling getAspect");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("aspect", aspect);
        uriVariables.put("oneID", oneID);
        uriVariables.put("partUniqueID", partUniqueID);
        String path = UriComponentsBuilder.fromPath("/catena-x/tdm/1.0/aspect/{aspect}/{oneID}/{partUniqueID}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<List<Object>> returnType = new ParameterizedTypeReference<List<Object>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
}

