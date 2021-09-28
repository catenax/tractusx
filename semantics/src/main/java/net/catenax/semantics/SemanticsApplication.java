/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import net.catenax.semantics.idsadapter.config.IdsAdapterConfigProperties;

/**
 * Main Adapter Application
 * TODO make sure openapi description is correct
 */
@SpringBootApplication
@EnableConfigurationProperties({IdsAdapterConfigProperties.class})
@ComponentScan(basePackages = {"net.catenax.semantics", "org.openapitools.configuration"})
public class SemanticsApplication {

	/**
	 * entry point if started as an app
	 * @param args command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(SemanticsApplication.class, args);
	}

}
