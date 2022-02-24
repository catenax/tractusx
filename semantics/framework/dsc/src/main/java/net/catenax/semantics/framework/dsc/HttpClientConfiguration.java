/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.dsc;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.AllArgsConstructor;
import net.catenax.semantics.framework.config.Config;
import net.catenax.semantics.framework.helpers.NaiveSSLSocketFactory;
import net.catenax.semantics.framework.dsc.client.api.*;
import net.catenax.semantics.framework.dsc.client.invoker.ApiClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.net.Proxy;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

/**
 * A spring configuration / bean factory for creating typed and proxiable http client objects
 * especially for talking to DSC.
 */
@Configuration
@AllArgsConstructor
public class HttpClientConfiguration {
    private final Config configurationData;

    @Bean
    Feign.Builder feignBuilder() throws NoSuchAlgorithmException, KeyManagementException {
        ApiClient apiClient = new ApiClient();
        NaiveSSLSocketFactory naiveSSLSocketFactory = new NaiveSSLSocketFactory("localhost");

        Client client=null;

        String proxyHost=System.getProperty("http.proxyHost");

        if(proxyHost!=null && !proxyHost.isEmpty()) {
            boolean noProxy = false;
            for (String noProxyHost : System.getProperty("http.nonProxyHosts","localhost").split("\\|")) {
                noProxy = noProxy || configurationData.getConnectorUrl().contains(noProxyHost.replace("*",""));
            }
            if (!noProxy) {
                client = new Client.Proxied(naiveSSLSocketFactory, null, new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyHost, Integer.parseInt(System.getProperty("http.proxyPort","80")))));
            }
        }

        if(client==null) {
            client = new Client.Default(naiveSSLSocketFactory,null);
        }

        Feign.Builder feignBuilder = apiClient.getFeignBuilder();
        apiClient.setBasePath(configurationData.getConnectorUrl());
        feignBuilder.client(client)
                .requestInterceptor(new BasicAuthRequestInterceptor(configurationData.getConnectorUser(),
                    configurationData.getConnectorPassword()));
        return feignBuilder;
    }

    @Bean
    ConnectorApi connectorApi(Feign.Builder feignBuilder) {
        return feignBuilder.target(ConnectorApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    OfferedResourcesApi offersApi(Feign.Builder feignBuilder) {
        return feignBuilder.target(OfferedResourcesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    CatalogsApi catalogApi(Feign.Builder feignBuilder) {
        return feignBuilder.target(CatalogsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    ContractsApi contractApi(Feign.Builder feignBuilder) {
        return feignBuilder.target(ContractsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    RulesApi rulesApi(Feign.Builder feignBuilder) {
        return feignBuilder.target(RulesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    EndpointsApi endpointsApi(Feign.Builder feignBuilder) {
        return feignBuilder.target(EndpointsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    RepresentationsApi representationsApi(Feign.Builder feignBuilder)  {
        return feignBuilder.target(RepresentationsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    RequestedResourcesApi requestedResourcesApi(Feign.Builder feignBuilder)  {
        return feignBuilder.target(RequestedResourcesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    ArtifactsApi artifactsApi(Feign.Builder feignBuilder)  {
        return feignBuilder.target(ArtifactsApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    MessagesApi messagesApi(Feign.Builder feignBuilder)  {
        return feignBuilder.target(MessagesApi.class, configurationData.getConnectorUrl());
    }

    @Bean
    AgreementsApi agreementsApi(Feign.Builder feignBuilder)  {
        return feignBuilder.target(AgreementsApi.class, configurationData.getConnectorUrl());
    }
}
