package org.microsoft.extension.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.prs.client.ApiClient;
import net.catenax.prs.client.ApiException;
import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import okhttp3.OkHttpClient;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;

import java.util.concurrent.TimeUnit;


public class PrsApiCaller implements DataReader {

    private static final String PARTS_TREE_BY_VIN = "PARTS_TREE_BY_VIN";
    private static final String PATH_PROPERTY = "path";
    private static final String VIN_PROPERTY = "vin";
    private static final String VIEW_PROPERTY = "view";
    private final PartsRelationshipServiceApi api;
    private final ObjectMapper objectMapper;

    public PrsApiCaller(String prsUrl) {
        var httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        var apiClient = new ApiClient(httpClient);
        apiClient.setBasePath(prsUrl);
        this.api = new PartsRelationshipServiceApi(apiClient);
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public String read(DataAddress address) {
        var properties = address.getProperties();
        String vin = properties.get(VIN_PROPERTY);
        String view = properties.get(VIEW_PROPERTY);
        String path = properties.get(PATH_PROPERTY);

        switch (path) {
            case PARTS_TREE_BY_VIN:
                try {
                    return objectMapper.writeValueAsString(api.getPartsTreeByVin(vin, view, "CE", 5));
                } catch (ApiException | JsonProcessingException e) {
                    throw new RuntimeException("Error when calling PRS API", e);
                }
            default:
                throw new RuntimeException("Query matching path " + path + " not found.");
        }
    }
}
