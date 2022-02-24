/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.transformers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.framework.*;
import net.catenax.semantics.framework.config.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.transform.TransformerHelper;

import javax.annotation.PostConstruct;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements an xml based transformer
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class XmlTransformer<Cmd extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation> implements Transformer {

    /**
     * the transformation helper
     */
    protected TransformerHelper transformerHelper=new TransformerHelper();

    /**
     * needs a configuration
     */
    protected final Config<Cmd, O, Ct, Co,T> configuration;

    /**
     * the names in this map are of form "sourceModel;targetMediaType;targetModel"
     */
    protected Map<String, Templates> transTemplates = new HashMap<>();

    /**
     * initialises the bean
     * @throws TransformerConfigurationException
     */
    @PostConstruct
    public void initTransTemplates() throws TransformerConfigurationException {
        if(configuration.getTransformations()!=null) {
            for (Map.Entry<String, T> transformation : configuration.getTransformations().entrySet()) {
                try {
                    String file=transformation.getValue().getFile();
                    InputStream inputStream=null;
                    if(file.startsWith("classpath:")) {
                        String resFile=file.substring(10);
                        Resource res=new ClassPathResource(resFile,getClass().getClassLoader());
                        if(!res.exists()) {
                            res=new ClassPathResource(resFile,getClass().getClassLoader().getParent());
                            if(!res.exists()) {
                                res = new ClassPathResource(resFile, Thread.currentThread().getContextClassLoader());
                                if (!res.exists()) {
                                    throw new IOException("Could not find " + resFile + " in the classloader environment.");
                                }
                            }
                        }
                        inputStream=res.getInputStream();
                    } else {
                        inputStream=new URL(file).openStream();
                    }
                    StreamSource streamSource = new StreamSource(inputStream);
                    Templates templates = transformerHelper.getTransformerFactory().newTemplates(streamSource);
                    transTemplates.put(transformation.getKey(), templates);
                    transTemplates.put(transformation.getValue().getSourceModel() + ";" + transformation.getValue().getTargetMediaType() + ";" + transformation.getValue().getTargetModel(), templates);
                } catch(NullPointerException | IOException e)  {
                    log.warn(String.format("can not initialize transformation: '%s' because of '%s' skipping", transformation, e.getMessage()));
                }
            }
        }
    }

    @Override
    public boolean canHandle(IdsMessage incoming, IdsRequest request, String targetModel) {
        String sourceMediaType=incoming.getMediaType();
        if (!sourceMediaType.endsWith("xml")) {
            log.debug("check failed because of non-xml media type source "+sourceMediaType);
            return false;
        }
        String immediateTransformation=request.getParameters().get("transformation");
        if(transTemplates.containsKey(immediateTransformation)) {
            log.debug("xml transformation has been directly chosen: "+immediateTransformation);
            return true;
        }
        String[] targetMedia = request.getAccepts().split(";");
        for(String target : targetMedia) {
            if(target.equals("*/*")) {
                boolean foundWildcard= transTemplates.keySet().stream().anyMatch(key -> {
                    boolean rightModel = key.startsWith(incoming.getModel()+";");
                    boolean rightTarget = key.endsWith(";"+targetModel);
                    return rightModel && rightTarget;
                });
                return foundWildcard;
            } else {
                String key = incoming.getModel() + ";" + target + ";" + targetModel;
                if (transTemplates.containsKey(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public IdsMessage transform(IdsMessage incoming, IdsRequest request, String targetModel) throws StatusException {
        BaseIdsMessage result=new BaseIdsMessage();
        String[] targetMedia = request.getAccepts().split(";");

        String plainTrafo=request.getParameters().get("transformation");
        if(transTemplates.containsKey(plainTrafo)) {
            result.setMediaType(targetMedia[0]);
            result.setModel(targetModel);
            extracted(incoming,result,transTemplates.get(plainTrafo));
        } else {
            for (String target : targetMedia) {
                String key = incoming.getModel() + ";" + target + ";" + targetModel;
                if(target.equals("*/*")) {
                    key = transTemplates.keySet().stream().filter(lkey -> {
                        boolean rightModel = lkey.startsWith(incoming.getModel()+";");
                        boolean rightTarget = lkey.endsWith(";"+targetModel);
                        return rightModel && rightTarget;
                    }).findFirst().get();
                    target=key.substring(key.indexOf(";")+1,key.lastIndexOf(";"));
                }
                if (transTemplates.containsKey(key)) {
                    result.setMediaType(target);
                    result.setModel(targetModel);
                    extracted(incoming, result, transTemplates.get(key));
                } else {
                    throw new StatusException("Assumed transformation with key "+key+" could not be found");
                }
            }
        }
        return result;
    }

    /**
     * does the actual transformation
     * @param incoming
     * @param result
     * @param template
     * @throws StatusException
     */
    private void extracted(IdsMessage incoming, BaseIdsMessage result, Templates template) throws StatusException {
        try {
            javax.xml.transform.Transformer trafo = template.newTransformer();
            for (Map.Entry<String, String> params : configuration.getTransformationParameters().entrySet()) {
                trafo.setParameter(params.getKey(), params.getValue());
            }
            StringResult traforesult = new StringResult();
            trafo.transform(new StringSource(incoming.getPayload()), traforesult);
            result.setPayload(traforesult.toString());
        } catch (TransformerException e) {
            throw new StatusException("Exception occured while transforming", e);
        }
    }
}
