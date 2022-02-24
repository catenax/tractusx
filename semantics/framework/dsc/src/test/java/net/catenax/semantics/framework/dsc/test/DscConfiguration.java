/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.dsc.test;

import net.catenax.semantics.framework.IdsConnector;
import net.catenax.semantics.framework.auth.BearerTokenOutgoingInterceptor;
import net.catenax.semantics.framework.config.*;
import net.catenax.semantics.framework.test.MockConfiguration;
import net.catenax.semantics.framework.test.MockConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * spring configuration of the framework tests
 */
@Configuration
@ComponentScan(basePackages = {"net.catenax.semantics.framework", "net.catenax.semantics.framework.dsc"})
public class DscConfiguration extends MockConfiguration {

    @Bean
    public Config<Command, Offer, Catalog, Contract, Transformation> getConfigurationData() {
        return super.getConfigurationData();
    }

    @Bean
    public DataSource getDataSource() {
        return super.getDataSource();
    }

    @Bean
    @Override
    public BearerTokenOutgoingInterceptor getInterceptor() {
        return super.getInterceptor();
    }
}
