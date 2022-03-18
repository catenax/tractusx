package org.eclipse.dataspaceconnector.apiwrapper.connector.sdk.service;

import jakarta.ws.rs.core.MultivaluedMap;
import java.util.Map;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.types.domain.edr.EndpointDataReference;

import java.io.IOException;

import static java.lang.String.format;

public class HttpProxyService {
    private final Monitor monitor;
    private final OkHttpClient httpClient;

    public HttpProxyService(Monitor monitor, OkHttpClient httpClient) {
        this.monitor = monitor;
        this.httpClient = httpClient;
    }

    public String sendGETRequest(EndpointDataReference dataReference, String subUrl, MultivaluedMap<String,String> parameters) throws IOException {
        var url = dataReference.getAddress();
        if(subUrl!=null && !subUrl.isEmpty()) {
            url=url+"/"+subUrl;
        }
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(Map.Entry<String, List<String>> param : parameters.entrySet()) {
            for (String value : param.getValue()) {
                httpBuilder=httpBuilder.addQueryParameter(param.getKey(), value);
            }
        }
        var requestBuilder = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode());

        var request = requestBuilder.build();

        return sendRequest(request);
    }

    public String sendPOSTRequest(EndpointDataReference dataReference, String subUrl, MultivaluedMap<String,String> parameters, String data, MediaType mediaType) throws IOException {
        var url = dataReference.getAddress();
        if(subUrl!=null && !subUrl.isEmpty()) {
            url=url+"/"+subUrl;
        }
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(Map.Entry<String, List<String>> param : parameters.entrySet()) {
            for (String value : param.getValue()) {
                httpBuilder=httpBuilder.addQueryParameter(param.getKey(), value);
            }
        }
        var requestBuilder = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode())
                .addHeader("Content-Type", mediaType.toString())
                .post(RequestBody.create(data, mediaType));


        var request=requestBuilder.build();

        return sendRequest(request);
    }

    private String sendRequest(Request request) throws IOException {
        var response = httpClient.newCall(request).execute();
        var body = response.body();

        if (!response.isSuccessful() || body == null) {
            monitor.warning(format("Data plane responded with error: %s %s", response.code(), body != null ? body.string() : ""));
            return null;
        }

        var bodyString = body.string();
        monitor.info("Data plane responded correctly: " + bodyString);
        return bodyString;
    }
}
