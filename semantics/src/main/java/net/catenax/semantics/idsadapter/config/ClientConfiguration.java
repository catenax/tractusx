/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.config;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.AllArgsConstructor;
import net.catenax.semantics.idsadapter.client.api.*;
import net.catenax.semantics.idsadapter.client.invoker.ApiClient;
import net.catenax.semantics.idsadapter.service.NaiveSSLSocketFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@AllArgsConstructor
public class ClientConfiguration {
    private final IdsAdapterConfigProperties idsAdapterConfigProperties;

    @Bean
    Feign.Builder feignBuilder() throws NoSuchAlgorithmException, KeyManagementException {
        ApiClient apiClient = new ApiClient();
        NaiveSSLSocketFactory naiveSSLSocketFactory = new NaiveSSLSocketFactory("localhost");
        Client.Default client = new Client.Default(naiveSSLSocketFactory,null);
        Feign.Builder feignBuilder = apiClient.getFeignBuilder();
        feignBuilder.client(client)
                .requestInterceptor(new BasicAuthRequestInterceptor(idsAdapterConfigProperties.getConnectorUser(),
                    idsAdapterConfigProperties.getConnectorPassword()));
        return feignBuilder;
    }

    @Bean
    ConnectorApi connectorApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(ConnectorApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    OfferedResourcesApi offersApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(OfferedResourcesApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    CatalogsApi catalogApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(CatalogsApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    ContractsApi contractApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(ContractsApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    RulesApi rulesApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(RulesApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    EndpointsApi endpointsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(EndpointsApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    RepresentationsApi representationsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(RepresentationsApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    RequestedResourcesApi requestedResourcesApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(RequestedResourcesApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    ArtifactsApi artifactsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(ArtifactsApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    MessagesApi messagesApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(MessagesApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }

    @Bean
    AgreementsApi agreementsApi(Feign.Builder feignBuilder) throws Exception {
        return feignBuilder.target(AgreementsApi.class, idsAdapterConfigProperties.getConnectorUrl());
    }
}
