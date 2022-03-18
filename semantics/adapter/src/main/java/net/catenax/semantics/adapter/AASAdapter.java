package net.catenax.semantics.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.semantics.framework.*;
import net.catenax.semantics.framework.aas.api.proxy.AssetIdentifierApiDelegate;
import net.catenax.semantics.framework.aas.model.*;
import net.catenax.semantics.framework.auth.BearerTokenOutgoingInterceptor;
import net.catenax.semantics.framework.auth.BearerTokenWrapper;
import net.catenax.semantics.framework.config.Config;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * An adapter for the AAS submodel API according to Catena-X Release 1
 */
@Service
public class AASAdapter extends BaseAdapter implements AssetIdentifierApiDelegate {

    /**
     * we need the token from the request
     */
    private final BearerTokenWrapper tokenWrapper;
    private final ObjectMapper objectMapper;

    /**
     * create the adapter in the environment
     * @param configurationData adapter config
     * @param connector connector abstraction
     * @param tokenWrapper session token access
     * @param objectMapper json conversion utility
     */
    public AASAdapter(Config configurationData, IdsConnector connector, BearerTokenWrapper tokenWrapper, ObjectMapper objectMapper) {
        super(configurationData);
        setIdsConnector(connector);
        this.tokenWrapper=tokenWrapper;
        this.objectMapper=objectMapper;
    }

    /**
     * getting the submodel requires a mapping to
     * internal assets
     * @param idsOffer name of the ids resource
     * @param assetIdentifier identifier of the twin
     * @param submodelIdentifier identifier of the representation
     * @param level how deep should the data be returned
     * @param content what mode should the data be returned
     * @param extent to which data should be returned
     * @return submodel
     */
    @Override
    public ResponseEntity<Submodel> getSubmodel(String idsOffer, String assetIdentifier, String submodelIdentifier, String level, String content, String extent, Map<String,String> otherParams) {
        if(!"deep".equals(level)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        if(!"value".equals(content)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        if(!"withBlobValue".equals(extent)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        IdsRequest request=new IdsRequest();
        String protocol=otherParams.getOrDefault("protocol","HTTP");
        request.setProtocol(protocol);
        String command=otherParams.getOrDefault("command","GET");
        request.setCommand(command);
        otherParams.put("aasid",assetIdentifier);
        request.setParameters(otherParams);
        request.setAccepts("application/json");
        request.setOffer(idsOffer);
        request.setRepresentation(submodelIdentifier);
        String artifact = otherParams.getOrDefault("artifact","default");
        request.setArtifact(artifact);
        request.setSecurityToken(tokenWrapper.getToken());
        request.setCallingConnectors("");
        IdsResponse idsResponse= idsConnector.perform(request);

        try {
            IdsMessage responseMessage = idsResponse.getMessage().get();
            String mediaType=responseMessage.getMediaType();
            Submodel model;
            if(responseMessage.getPayload()!=null) {
                model=objectMapper.readValue(responseMessage.getPayload(),Submodel.class);
            } else {
                model=new Submodel();
            }
            return ResponseEntity.ok(model);
        } catch(InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @Override
    public ResponseEntity<OperationResult> invokeOperation(String idsResourceId, String  assetIdentifier,
                                                    String  submodelIdentifier,
                                                    String  idShortPath,
                                                    OperationRequest  body,
                                                    Boolean  async,
                                                    String  content, Map<String,String> otherParams) {
        if(!"value".equals(content)) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        if(async) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        IdsRequest request=new IdsRequest();
        String protocol=otherParams.getOrDefault("protocol","HTTP");
        request.setProtocol(protocol);
        request.setCommand(otherParams.getOrDefault("command","POST"));
        otherParams.put("aasid",assetIdentifier);
        request.setParameters(otherParams);
        request.setAccepts("application/json");
        request.setOffer(idsResourceId);
        try {
            request.setPayload(objectMapper.writeValueAsString(body));
            request.setRepresentation(submodelIdentifier);
            request.setArtifact(idShortPath);
            request.setSecurityToken(tokenWrapper.getToken());
            request.setCallingConnectors("");
            IdsResponse idsResponse= idsConnector.perform(request);

            IdsMessage responseMessage = idsResponse.getMessage().get();
            String mediaType=responseMessage.getMediaType();
            OperationResult res;
            if(responseMessage.getPayload()!=null) {
                res=objectMapper.readValue(responseMessage.getPayload(),OperationResult.class);
            } else {
                res=new OperationResult();
            }
            return ResponseEntity.ok(res);
        } catch(InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }
}
