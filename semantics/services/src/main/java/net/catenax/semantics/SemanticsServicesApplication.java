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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Adapter Application
 * TODO make sure openapi description is correct, referrer-header should give us a hint.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"net.catenax.semantics", "org.openapitools.configuration"})
public class SemanticsServicesApplication {

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurer(){
			@Override
			public void addCorsMappings(CorsRegistry registry) {
			  registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
			}			  
		};
	 }

	/**
	 * entry point if started as an app
	 * @param args command line
	 */
	public static void main(String[] args) {
		new SpringApplication(SemanticsServicesApplication.class).run(args);
	}

}
