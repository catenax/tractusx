/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.aas.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.catenax.semantics.aas.api.registry.LookupApiDelegate;
import net.catenax.semantics.aas.api.registry.RegistryApiDelegate;
import net.catenax.semantics.framework.aas.api.RegistryAndDiscoveryInterfaceApi;
import net.catenax.semantics.framework.aas.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The AAS Proxy implements the service layer behind the web protocol layer.
 */
@Service
@RequiredArgsConstructor
public class RegistryAndDiscoveryProxy implements LookupApiDelegate, RegistryApiDelegate {

    /**
     * the central registry that we delegate to or that redirects us
     */
    protected final RegistryAndDiscoveryInterfaceApi delegate;
    /**
     * we got a rewrite storage for endpoints
     */
    protected final RewriteStorage storage;

    /**
     * config data
     */
    protected final ConfigurationData config;


    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return LookupApiDelegate.super.getObjectMapper();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return LookupApiDelegate.super.getRequest();
    }

    @Override
    public Optional<String> getAcceptHeader() {
        return LookupApiDelegate.super.getAcceptHeader();
    }

    @Override
    public ResponseEntity<Void> deleteAssetAdministrationShellDescriptorById(String aasIdentifier) {
        delegate.deleteAssetAdministrationShellDescriptorById(aasIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteSubmodelDescriptorById(String aasIdentifier, String submodelIdentifier) {
        delegate.deleteSubmodelDescriptorById(aasIdentifier, submodelIdentifier);
        return ResponseEntity.ok().build();
    }

    /**
     * rewrites a set of shell descriptors
     * @param collection
     * @return
     */
    protected AssetAdministrationShellDescriptorCollection rewrite(AssetAdministrationShellDescriptorCollection collection) {
        collection.setItems(collection.getItems().stream().map( descriptor -> rewrite(descriptor)).collect(Collectors.toList()));
        return collection;
    }

    /**
     * rewrites a shell descriptor
     * @param descriptor
     * @return
     */
    protected AssetAdministrationShellDescriptor rewrite(AssetAdministrationShellDescriptor descriptor) {
        descriptor.setSubmodelDescriptors(descriptor.getSubmodelDescriptors().stream().map(submodel->rewrite(descriptor.getIdentification(),submodel)).collect(Collectors.toList()));
        return descriptor;
    }

    /**
     * rewrites a submodel descriptor
     * @param assetId
     * @param descriptor
     * @return
     */
    protected SubmodelDescriptor rewrite(String assetId,SubmodelDescriptor descriptor) {
        descriptor.setEndpoints(descriptor.getEndpoints().stream().map(endpoint -> rewrite(assetId,descriptor.getIdentification(),endpoint)).collect(Collectors.toList()));
        return descriptor;
    }

    /**
     * rewrites an endpoint
     * @param assetId
     * @param submodelId
     * @param endpoint
     * @return
     */
    protected Endpoint rewrite(String assetId, String submodelId, Endpoint endpoint) {
        ProtocolInformation pi=endpoint.getProtocolInformation();
        String endpointAddress=pi.getEndpointAddress();
        String query="";
        int params=endpointAddress.indexOf(submodelId);
        if(params>=0) {
            query=endpointAddress.substring(params+submodelId.length());
            endpointAddress=endpointAddress.substring(0,params+submodelId.length());
        }
        storage.setEndpoint(assetId,submodelId,endpointAddress);
        // TODO get from configuration
        pi.setEndpointAddress(config.getProxyUrl()+"/shells/"+assetId+"/aas/"+submodelId+query);
        if(config.getProxyUrl().startsWith("https")) {
            pi.setEndpointProtocol("HTTP/S");
        } else {
            pi.setEndpointProtocol("HTTP");
        }
        pi.setEndpointProtocolVersion("1.1");
        return endpoint;
    }

    /**
     * invoke delegate and rewrite the endpoints
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public ResponseEntity<AssetAdministrationShellDescriptorCollection> getAllAssetAdministrationShellDescriptors(Integer page, Integer pageSize) {
        try {
            return ResponseEntity.ok(rewrite(delegate.getAllAssetAdministrationShellDescriptors(page, pageSize)));
        } catch(feign.FeignException e) {
            var response=ResponseEntity.status(e.status());
            if(e.responseBody().isPresent()) {
                response.body(e.responseBody().get().toString());
            }
            return response.build();
        }
    }

    /**
     * invoke delegate and rewrite the endpoints
     * @param aasIdentifier
     * @return
     */
    @Override
    public ResponseEntity<List<SubmodelDescriptor>> getAllSubmodelDescriptors(String aasIdentifier) {
        return ResponseEntity.ok(delegate.getAllSubmodelDescriptors(aasIdentifier).stream().map(subModel->rewrite(aasIdentifier,subModel)).collect(Collectors.toList()));
    }

    /**
     * invoke and rewrite the endpoints
     * @param aasIdentifier
     * @return
     */
    @Override
    public ResponseEntity<AssetAdministrationShellDescriptor> getAssetAdministrationShellDescriptorById(String aasIdentifier) {
        return ResponseEntity.ok(rewrite(delegate.getAssetAdministrationShellDescriptorById(aasIdentifier)));
    }

    /**
     * invoke and rewrite the endpoints
     * @param aasIdentifier
     * @param submodelIdentifier
     * @return
     */
    @Override
    public ResponseEntity<SubmodelDescriptor> getSubmodelDescriptorById(String aasIdentifier, String submodelIdentifier) {
        return ResponseEntity.ok(rewrite(aasIdentifier,delegate.getSubmodelDescriptorById(aasIdentifier, submodelIdentifier)));
    }

    /**
     * invoke and rewrite
     * @param assetAdministrationShellDescriptor
     * @return
     */
    @Override
    public ResponseEntity<AssetAdministrationShellDescriptor> postAssetAdministrationShellDescriptor(AssetAdministrationShellDescriptor assetAdministrationShellDescriptor) {
        return ResponseEntity.ok(rewrite(delegate.postAssetAdministrationShellDescriptor(assetAdministrationShellDescriptor)));
    }

    /**
     * invoke and rewrite
     * @param aasIdentifier
     * @param submodelDescriptor
     * @return
     */
    @Override
    public ResponseEntity<SubmodelDescriptor> postSubmodelDescriptor(String aasIdentifier, SubmodelDescriptor submodelDescriptor) {
        return ResponseEntity.ok(rewrite(aasIdentifier,delegate.postSubmodelDescriptor(submodelDescriptor,aasIdentifier)));
    }

    @Override
    public ResponseEntity<Void> putAssetAdministrationShellDescriptorById(String aasIdentifier, AssetAdministrationShellDescriptor assetAdministrationShellDescriptor) {
        delegate.putAssetAdministrationShellDescriptorById(assetAdministrationShellDescriptor, aasIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> putSubmodelDescriptorById(String aasIdentifier, String submodelIdentifier, SubmodelDescriptor submodelDescriptor) {
        delegate.putSubmodelDescriptorById(submodelDescriptor,aasIdentifier, submodelIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllAssetLinksById(String aasIdentifier) {
        delegate.deleteAllAssetLinksById(aasIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<String>> getAllAssetAdministrationShellIdsByAssetLink(List<IdentifierKeyValuePair> assetIds) {
        return ResponseEntity.ok(delegate.getAllAssetAdministrationShellIdsByAssetLink(assetIds));
    }

    @Override
    public ResponseEntity<List<IdentifierKeyValuePair>> getAllAssetLinksById(String aasIdentifier) {
        return ResponseEntity.ok(delegate.getAllAssetLinksById(aasIdentifier));
    }

    @Override
    public ResponseEntity<List<IdentifierKeyValuePair>> postAllAssetLinksById(String aasIdentifier, List<IdentifierKeyValuePair> identifierKeyValuePair) {
        return ResponseEntity.ok(delegate.postAllAssetLinksById(identifierKeyValuePair,aasIdentifier));
    }
}
