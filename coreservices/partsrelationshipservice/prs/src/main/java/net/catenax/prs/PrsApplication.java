//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 */
@SpringBootApplication
@OpenAPIDefinition(
    servers = {@Server(
            url = "http://localhost:8080"
            )},
    info = @Info(
            title = "Catena-X Parts Relationship Service",
            version = PrsApplication.API_VERSION,
            description = "API to retrieve parts tree information."
    )
)
public class PrsApplication {

    /** The PRS API version. */
    public static final String API_VERSION = "v0.1";

    /** The URL prefix for PRS API URLs. */
    public static final String API_PREFIX = "api/" + API_VERSION;

    /**
     * Entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(PrsApplication.class, args);
    }

}
