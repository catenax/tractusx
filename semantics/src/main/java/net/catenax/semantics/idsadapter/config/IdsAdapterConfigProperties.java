/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.config;

import lombok.Data;
import net.catenax.semantics.idsadapter.restapi.dto.Catalog;
import net.catenax.semantics.idsadapter.restapi.dto.Contract;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.Source;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("idsadapter")
@Data
public class IdsAdapterConfigProperties {
    private boolean offerOnStart=false;
    private boolean registerOnStart=false;

    private String connectorUrl;
    private String serviceUrl;
    private String portalUrl;
    private String connectorUser;
    private String connectorPassword;
    private String publisher;
    
    private Map<String, Catalog> catalogs = new HashMap<>();

    private Map<String, Offer> offers=new HashMap<>();

    private Map<String, Contract> contracts = new HashMap<>();

    private Map<String, Source> twins= new HashMap<>();
}
