/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.adapter;

import net.catenax.semantics.framework.edc.EdcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * Main Adapter Application
 * TODO make sure openapi description is correct, referrer-header should give us a hint.
 */
@SpringBootApplication
@EnableConfigurationProperties({ConfigurationData.class})
@ComponentScan(basePackages = {"net.catenax.semantics.adapter", "net.catenax.semantics.framework", "org.openapitools.configuration"})
public class Application {

	/**
	 * add a webmvc configurer allowing local cors
	 * @return webmvc
	 */
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
		// bootstrap EDC
		EdcService.bootstrap();
		new SpringApplication(Application.class).run(args);
	}

}
