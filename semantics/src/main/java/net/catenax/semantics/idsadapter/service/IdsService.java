/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.idsadapter.client.api.AgreementsApi;
import net.catenax.semantics.idsadapter.client.api.ArtifactsApi;
import net.catenax.semantics.idsadapter.client.api.CatalogsApi;
import net.catenax.semantics.idsadapter.client.api.ContractsApi;
import net.catenax.semantics.idsadapter.client.api.MessagesApi;
import net.catenax.semantics.idsadapter.client.api.OfferedResourcesApi;
import net.catenax.semantics.idsadapter.client.api.RepresentationsApi;
import net.catenax.semantics.idsadapter.client.api.RulesApi;
import net.catenax.semantics.idsadapter.client.model.ArtifactDesc;
import net.catenax.semantics.idsadapter.client.model.ArtifactView;
import net.catenax.semantics.idsadapter.client.model.CatalogDesc;
import net.catenax.semantics.idsadapter.client.model.CatalogView;
import net.catenax.semantics.idsadapter.client.model.ContractDesc;
import net.catenax.semantics.idsadapter.client.model.ContractRuleDesc;
import net.catenax.semantics.idsadapter.client.model.ContractRuleView;
import net.catenax.semantics.idsadapter.client.model.ContractView;
import net.catenax.semantics.idsadapter.client.model.Link;
import net.catenax.semantics.idsadapter.client.model.Links;
import net.catenax.semantics.idsadapter.client.model.OfferedResourceDesc;
import net.catenax.semantics.idsadapter.client.model.OfferedResourceView;
import net.catenax.semantics.idsadapter.client.model.PagedModelCatalogView;
import net.catenax.semantics.idsadapter.client.model.PagedModelContractView;
import net.catenax.semantics.idsadapter.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.idsadapter.client.model.RepresentationDesc;
import net.catenax.semantics.idsadapter.client.model.RepresentationView;
import net.catenax.semantics.idsadapter.config.IdsAdapterConfigProperties;
import net.catenax.semantics.idsadapter.restapi.dto.Catalog;
import net.catenax.semantics.idsadapter.restapi.dto.Contract;
import net.catenax.semantics.idsadapter.restapi.dto.ContractRule;
import net.catenax.semantics.idsadapter.restapi.dto.DataSource;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.ReceiveRequest;
import net.catenax.semantics.idsadapter.restapi.dto.Representation;
import net.catenax.semantics.idsadapter.restapi.dto.Source;
import net.catenax.semantics.tools.ResultSetToJsonStreamer;

/**
 * A service that manages the interaction with the connector
 */
@Service
@AllArgsConstructor
@Slf4j
public class IdsService {

    @Autowired   
    private javax.sql.DataSource defaultDataSource;

    /** adapter config */
    private final IdsAdapterConfigProperties adapterProperties;

    /** these are clients to the different endpoints of the connector */
    private final ContractsApi contractsApi;
    private final OfferedResourcesApi offeredResourcesApi;
    private final CatalogsApi catalogsApi;
    private final RulesApi rulesApi;
    private final RepresentationsApi representationsApi;
    private final ArtifactsApi artifactsApi;
    private final MessagesApi messagesApi;
    private final ObjectMapper objectMapper;
    private final AgreementsApi agreementsApi;

    /** standard access policy */
    private final String PROVIDE_ACCESS_POLICY =
            "{ \"@context\" : { \"ids\" : \"https://w3id.org/idsa/core/\", \"idsc\" : \"https://w3id.org/idsa/code/\" }, \"@type\" : \"ids:Permission\", \"@id\" : \"https://w3id.org/idsa/autogen/permission/658ca300-4042-4804-839a-3c9548dcc26e\", \"ids:action\" : [ { \"@id\" : \"https://w3id.org/idsa/code/USE\" } ], \"ids:description\" : [ { \"@value\" : \"provide-access\", \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\" } ], \"ids:title\" : [ { \"@value\" : \"Allow Data Usage\", \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\" } ] }";


    /**
     * extract the UUID from this object
     * @param links link section of the object
     * @return UUID
     */
    private UUID getSelfIdFromLinks(Links links) {
        return getIdFromLink(links.get("self"));
    }

    /**
     * extract the UUID from a given URI link
     * @param link link
     * @return UUID as the last part of the URI
     */
    private UUID getIdFromLink(Link link) {
        String href = link.getHref();
        return UUID.fromString(href.substring(href.lastIndexOf('/') + 1));
    }

    /**
     * extract the absolute URI of this object
     * @param links link section of the object
     * @return absolute uri
     */
    public String getHrefFromSelfLinks(Links links) {
        return links.get("self").getHref();
    }

    /**
     * extract the absolute URI of this object as a list
     * @param links link section of the object
     * @return absolute uri as a list
     */
    public List<String> getHrefListFromSelfLinks(Links links) {
        return Collections.singletonList(getHrefFromSelfLinks(links));
    }

    /**
     * when the service start it may read its config
     * and automatically publish stuff
     */
    @PostConstruct
    public void setup() {
        try {
            adapterProperties.getOffers().entrySet().forEach( source -> {
                getOrCreateOffer(source.getKey(),source.getValue());
            } );
        } catch(Exception e) {
            log.error("Could not setup sources in connector. Maybe it is not active?",e);
        }
    }


    /**
     * get or create a catalog
     * @param title key of the catalogue, must be no-null
     * @oaram catalog representation, maybe null if internal configuration should be used
     * @return existing or new contract representation
     */
    public Catalog getOrCreateCatalog(String title, Catalog catalog) {
        // is it described in our config?
        if(catalog==null) {
            if(adapterProperties.getCatalogs().containsKey(title)) {
                // take the configured template
                catalog = adapterProperties.getCatalogs().get(title);
            } else {
                catalog = new Catalog();
            }
        }
        PagedModelCatalogView catalogsView = catalogsApi.getAll10(null, null);
        for (CatalogView catalogView : catalogsView.getEmbedded().getCatalogs()) {
            if (title.equals(catalogView.getTitle())) {
                catalog.setDescription(catalogView.getDescription());
                catalog.setId(getSelfIdFromLinks(catalogView.getLinks()));
                catalog.setUri(getHrefFromSelfLinks(catalogView.getLinks()));
                return catalog;
            }
        }
        CatalogDesc catalogDesc = new CatalogDesc();
        catalogDesc.setTitle(title);
        catalogDesc.setDescription(catalog.getDescription());
        CatalogView catalogView =catalogsApi.create9(catalogDesc);
        catalog.setId(getSelfIdFromLinks(catalogView.getLinks()));
        catalog.setUri(getHrefFromSelfLinks(catalogView.getLinks()));
        return catalog;
    }

    /**
     * get or create a contract
     * @param title key of the contract
     * @param contract blueprint of the contract
     * @return existing or new contract representation
     */
    public Contract getOrCreateContract(String title, Contract contract) {
        // is it described in our config?
        if(contract==null) {
            if(adapterProperties.getContracts().containsKey(title)) {
                // take the configured template
                contract = adapterProperties.getContracts().get(title);
            } else {
                contract = new Contract();
            }
        }
        PagedModelContractView contractsView = contractsApi.getAll8(null, null);
        for (ContractView contractView : contractsView.getEmbedded().getContracts()) {
            if (title.equals(contractView.getTitle())) {
                contract.setId(getSelfIdFromLinks(contractView.getLinks()));
                contract.setUri(getHrefFromSelfLinks(contractView.getLinks()));
                contract.setDescription(contractView.getDescription());
                contract.setConsumer(contractView.getConsumer());
                contract.setStart(contractView.getStart());
                contract.setEnd(contractView.getEnd());
                return contract;
            }
        }
        ContractDesc contractDesc = new ContractDesc();
        contractDesc.setTitle(title);
        contractDesc.setDescription(contract.getDescription());
        contractDesc.setStart(contract.getStart());
        contractDesc.setEnd(contract.getEnd());
        contractDesc.setProvider(adapterProperties.getConnectorUrl());
        contractDesc.setConsumer(contract.getConsumer());
        ContractView contractView = contractsApi.create7(contractDesc);
        contract.setId(getSelfIdFromLinks(contractView.getLinks()));
        contract.setUri(getHrefFromSelfLinks(contractView.getLinks()));

        for(Map.Entry<String, ContractRule> rule: contract.getRules().entrySet()) {
            ContractRuleDesc contractRuleDesc = new ContractRuleDesc();
            contractRuleDesc.setTitle(rule.getKey());
            contractRuleDesc.setDescription(rule.getValue().getDescription());
            contractRuleDesc.setValue(rule.getValue().getValue());
            ContractRuleView ruleView = rulesApi.create1(contractRuleDesc);
            rule.getValue().setId(getSelfIdFromLinks(ruleView.getLinks()));
            rule.getValue().setUri(getHrefFromSelfLinks(ruleView.getLinks()));
        }
        List<String> ruleLinks=contract.getRules().values().stream().map( rule -> rule.getUri()).collect(Collectors.toList());
        contractsApi.addResourcesRule(ruleLinks, contract.getId());
        return contract;
    }

    /**
     * registers a new (re-)source in the ids
     * @param title key of the offer
     * @param offer blueprint of the offer
     * @return new or already existing offer
     */
    public Offer getOrCreateOffer(String title, Offer offer) {
        // is it described in our config?
        if(offer==null) {
            if(adapterProperties.getOffers().containsKey(title)) {
                // take the configured template
                offer = adapterProperties.getOffers().get(title);
            } else {
                offer = new Offer();
            }
        }
        PagedModelOfferedResourceView offersView = offeredResourcesApi.getAll5(null, null);
        for (OfferedResourceView offerView : offersView.getEmbedded().getResources()) {
            if (title.equals(offerView.getTitle())) {
                offer.setId(getSelfIdFromLinks(offerView.getLinks()));
                offer.setUri(getHrefFromSelfLinks(offerView.getLinks()));
                offer.setDescription(offerView.getDescription());
                offer.setKeywords(offerView.getKeywords());
                offer.setLanguage(offerView.getLanguage());
                offer.setPaymentMethod(offerView.getPaymentModality().getValue());
                offer.setLicense(offerView.getLicense());
                for (RepresentationView representationView : offeredResourcesApi.getResource12(offer.getId(), null, null).getEmbedded().getRepresentations()) {
                    Representation representation;
                    if (offer.getRepresentations().containsKey(representationView.getTitle())) {
                        representation = offer.getRepresentations().get(representationView.getTitle());
                    } else {
                        representation = new Representation();
                    }
                    representation.setId(getSelfIdFromLinks(representationView.getLinks()));
                    representation.setUri(getHrefFromSelfLinks(representationView.getLinks()));
                    representation.setDescription(representationView.getDescription());
                    representation.setLanguage(representationView.getLanguage());
                    representation.setMediaType(representationView.getMediaType());
                    //representation.setModel(representationView.get)
                    offer.getRepresentations().put(representationView.getTitle(), representation);
                    for (ArtifactView artifactView : representationsApi.getResource10(representation.getId(), null, null).getEmbedded().getArtifacts()) {
                        Source source;
                        if (representation.getSources().containsKey(artifactView.getTitle())) {
                            source = representation.getSources().get(artifactView.getTitle());
                        } else {
                            source = new Source();
                        }
                        source.setId(getSelfIdFromLinks(artifactView.getLinks()));
                        source.setUri(getHrefFromSelfLinks(artifactView.getLinks()));
                        source.setDescription(artifactView.getDescription());
                        representation.getSources().put(artifactView.getTitle(), source);
                    }
                }
                return offer;
            }
        }
            OfferedResourceDesc resource = new OfferedResourceDesc();
            resource.setTitle(title);
            resource.setDescription(offer.getDescription());
            resource.setKeywords(offer.getKeywords());
            resource.setPublisher(adapterProperties.getPublisher());
            resource.setLanguage(offer.getLanguage());
            resource.setPaymentMethod(OfferedResourceDesc.PaymentMethodEnum.valueOf(offer.getPaymentMethod()));
            resource.setLicense(offer.getLicense());
            resource.setEndpointDocumentation(adapterProperties.getServiceUrl());
            OfferedResourceView resourceView = offeredResourcesApi.create4(resource);
            offer.setId(getSelfIdFromLinks(resourceView.getLinks()));
            offer.setUri(getHrefFromSelfLinks(resourceView.getLinks()));

            Catalog catalog=getOrCreateCatalog(offer.getCatalog(),null);
            if(catalog!=null) {
                catalogsApi.addResourcesOffer(Collections.singletonList(offer.getUri()), catalog.getId());
            }

            Contract contract=getOrCreateContract(offer.getContract(),null);
            if(contract!=null) {
                contractsApi.addResourcesOffers(Collections.singletonList(offer.getUri()), contract.getId());
            }

            for (Map.Entry<String, Representation> representationEntry : offer.getRepresentations().entrySet()) {
                Representation representation = representationEntry.getValue();
                // Add representation
                RepresentationDesc representationDesc = new RepresentationDesc();
                representationDesc.setTitle(representationEntry.getKey());
                representationDesc.setLanguage(representation.getLanguage());
                representationDesc.setDescription(representation.getDescription());
                representationDesc.setMediaType(representation.getMediaType());
                representationDesc.setStandard(representation.getModel());
                RepresentationView representationView = representationsApi.create3(representationDesc);
                representation.setId(getSelfIdFromLinks(representationView.getLinks()));
                representation.setUri(getHrefFromSelfLinks(representationView.getLinks()));

                for (Map.Entry<String, Source> path : representation.getSources().entrySet()) {
                    Source source = path.getValue();
                    ArtifactDesc artifactDesc = new ArtifactDesc();
                    artifactDesc.setTitle(path.getKey());
                    artifactDesc.setDescription(source.getDescription());
                    artifactDesc.setAccessUrl(adapterProperties.getServiceUrl()+"/download?offer="+title+"&representation="+representationEntry.getKey()+"&source="+path.getKey());
                    ArtifactView artifactView = artifactsApi.create11(artifactDesc);
                    source.setId(getSelfIdFromLinks(artifactView.getLinks()));
                    source.setUri(getHrefFromSelfLinks(artifactView.getLinks()));
                }
                List<String> artifactList=representation.getSources().values().stream().map( source -> source.getUri()).collect(Collectors.toList());
                representationsApi.addResourcesArtifact(artifactList, representation.getId());
            }
            List<String> representationsList=offer.getRepresentations().values().stream().map( rep -> rep.getUri()).collect(Collectors.toList());
            offeredResourcesApi.addResourcesRepresentation(representationsList, offer.getId());
            return offer;
    }

    /**
     * downloads a source file
     * @param response the outputstream to put the resource into
     * @param file optional file name
     * @param transformation optional transformation
     * @param offer optional offer name
     * @param representation optional representation name
     * @param source optional source name
     * @return the resulting media type of the file
     */
    public String downloadForAgreement(OutputStream response, String mediaType, String file, String transformation, String offer, String representation, String source, String param) {
        log.info("Received a download request into stream "+response+" with default mediaType "+mediaType);
        if(file==null) {
            try {
                log.info("Looking up OFFER "+offer);

                Thread.sleep(500);
                
                Offer off = adapterProperties.getOffers().get(offer);
               
                log.info("Looking up REPRESENTATION "+representation);
                
                Thread.sleep(500);

                Representation rep = off.getRepresentations().get(representation);

                log.info("Looking up SOURCE "+source);

                Thread.sleep(500);

                Source so = rep.getSources().get(source);
                if(so != null) {
                    if(so.getType().equals("file")) {
                        file=so.getFile();
                        transformation=so.getTransformation();
                        mediaType = handleSourceFile(response, mediaType, file, transformation);
                    }
                    if(so.getType().equals("jdbc")) {
                        mediaType = handleSourceJdbc(response, mediaType, so, param);
                    }
                }
            } catch (Exception e) {
                log.error("Source not been found. Either no file was given or the offer/representation/source path does not exist. Leaving empty.",e);
                return mediaType;
            }
        } else {
            mediaType = handleSourceFile(response, mediaType, file, transformation);
        }

        return mediaType;
    }

    private String handleSourceJdbc(OutputStream response, String mediaType, Source so, String param) throws ClassNotFoundException, SQLException {
        //getting default db connection
        Connection conn = defaultDataSource.getConnection();
        DataSource ds = so.getDatasource();
        if(ds != null && !ds.getDriverClassName().isEmpty()) {
            Class.forName (ds.getDriverClassName()); 
            conn = DriverManager.getConnection (ds.getUrl(), ds.getUsername(),ds.getPassword());
            log.info("using configured DataSource Connection: " + conn.toString());
        } else {
            log.info("using default DataSource Connection: " + conn.toString());
        }
        Statement stmt = conn.createStatement();
        String sql = so.getAlias();
        if (param != null && sql.contains("{0}")) {
            sql = java.text.MessageFormat.format(sql, "'"+param+"'");
        } else {
            sql = sql.replaceAll("where.*", "");
        }
        log.info(sql);
        ResultSet resultSet = stmt.executeQuery(sql);
        mediaType="application/json";
        (new ResultSetToJsonStreamer(response)).extractData(resultSet);
        return mediaType;
    }

    private String handleSourceFile(OutputStream response, String mediaType, String file, String transformation)
            throws TransformerFactoryConfigurationError {
        log.info("Accessing FILE source "+file);
        URL resource = getClass().getClassLoader().getResource(file);
        if(resource==null) {
            log.error("File "+file+" could not bee found. Leaving empty.");
            return mediaType;
        }
        try {
            Thread.sleep(500);
            InputStream resourceStream=resource.openStream();
            mediaType="text/xml";
            log.info("Media Type changed to "+mediaType);
            if(transformation!=null) {
                log.info("Accessing TRANSFORMATION source "+transformation);
                Thread.sleep(500);
                URL sheet = getClass().getClassLoader().getResource(transformation);
                if (sheet != null) {
                    
                    log.info("Setting up XSLT style transformation");
                    
                    Thread.sleep(500);

                    mediaType="application/json";
                    log.info("Media Type changed to "+mediaType);
                    
                    javax.xml.transform.Source xslt = new StreamSource(sheet.openStream());
                    javax.xml.transform.Source xml = new StreamSource(resourceStream);
                    javax.xml.transform.Result out = new StreamResult(response);
                    javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
                    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

                    javax.xml.transform.Transformer transformer = factory.newTransformer(xslt);
                    transformer.transform(xml, out);
                    resourceStream.close();
                    return mediaType;
                } else {
                    log.warn("Transformation " + transformation + " could not be found. Copying the original.");
                }
            }
            resourceStream.transferTo(response);
            resourceStream.close();
        } catch (InterruptedException | IOException | javax.xml.transform.TransformerException e) {
            log.error("download & transform error. Leaving empty.", e);
        }
        return mediaType;
    }

        /**
         * receive an artifact
         * @param receiveRequest
         * @return
         */
        public Object receiveResource(ReceiveRequest receiveRequest) {
            Map<String,Object> desc = (Map<String, Object>) messagesApi.sendDescriptionRequest(receiveRequest.getConnectorUrl(), receiveRequest.getResourceId());
            List<Map<String,Object>> contractOffers = (List<Map<String, Object>>) desc.get("ids:contractOffer");
            List<Map<String,Object>> permissions = (List<Map<String, Object>>) contractOffers.get(0).get("ids:permission");
            List<Map<String,Object>> representations = (List<Map<String, Object>>) desc.get("ids:representation");
            // search one artifactId and put in contract target
            String artifactId = null;
            try {
                log.info("description "+objectMapper.writeValueAsString(desc));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            for (Map<String,Object> representation : representations) {
                List<Map<String,Object>> instances = (List<Map<String, Object>>) representation.get("ids:instance");
                for (Map<String,Object> instance : instances) {
                    artifactId = (String) instance.get("@id");
                    break;
                }
                if (artifactId!=null) {
                    break;
                }
            }
            if (artifactId==null) {
                log.error("artifact id not found in description");
                throw new RuntimeException("can not find artifactid in description");
            }
            permissions.get(0).put("ids:target",artifactId);
            List<Object> body = new ArrayList<>(permissions);
            Map<String, Object> agreement = (Map<String, Object>) messagesApi.sendContract(body, receiveRequest.getConnectorUrl(),
                    Collections.singletonList(receiveRequest.getResourceId()),
                    Collections.singletonList(artifactId), false);
            return agreement;
        }

    }
