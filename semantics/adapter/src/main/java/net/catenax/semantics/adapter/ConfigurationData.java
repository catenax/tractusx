/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.adapter;

import lombok.Data;
import net.catenax.semantics.framework.config.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Simple Adapter uses the default configuration
 */
@ConfigurationProperties(prefix="idsadapter")
@Data
public class ConfigurationData
        extends net.catenax.semantics.framework.config.ConfigurationData<Command, Offer, Catalog, Contract, Transformation> {
}
