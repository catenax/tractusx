/** 
 * Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
 *
 * See the AUTHORS file(s) distributed with this work for additional
 * information regarding authorship.
 *
 * See the LICENSE file(s) distributed with this work for
 * additional information regarding license terms.
 */

package net.catenax.semantics.idsadapter.restapi.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.idsadapter.client.api.ConnectorApi;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.ReceiveRequest;
import net.catenax.semantics.idsadapter.service.IdsService;
import springfox.documentation.annotations.ApiIgnore;

import io.swagger.annotations.*;

/**
 * Controller to implement the Adapters REST interface One face to the "business
 * partner" for investigating whats inside the adapter and one face to the "ids"
 * connector for accessing/triggering data accesses.
 */
@Controller
@RequestMapping("${openapi.semanticHub.base-path:/api/v1/adapter}")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Slf4j
@Api(tags="Adapter", value = "adapter", description = "Simple Semantic Adapter API")
public class AdapterController {
    private final IdsService idsService;
    private final ConnectorApi connectorApi;

    /**
     * Simple hello
     * 
     * @return hello response
     */
    @GetMapping(value = "/hello", produces = "text/plain")
    public ResponseEntity<String> hello() {
        log.info("getting hello");
        return ResponseEntity.ok("hello");
    }

    /**
     * obtain info of the associated ids connector
     * 
     * @return self-description response
     */
    @GetMapping(value = "/idsinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> idsInfo() {
        return ResponseEntity.ok(connectorApi.getPrivateSelfDescription());
    }

    /**
     * publish a preconfigured source
     * 
     * @param name  source specification
     * @param offer body
     * @return register response
     */
    @PostMapping(value = "/register/:name", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Offer> registerResource(@PathVariable String name, @RequestBody Offer offer) {
        return ResponseEntity.ok(idsService.getOrCreateOffer(name, offer));
    }

    /**
     * Receive data from some external source
     * 
     * @param receiveRequest body
     * @return receive response
     */
    @PostMapping(value = "/receive", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> receiveResource(@RequestBody ReceiveRequest receiveRequest) {
        return ResponseEntity.ok(idsService.receiveResource(receiveRequest));
    }

    /**
     * Download an offered, possibly aspect-json transformed source
     * 
     * @param parameters map of the parameters
     * @return response including the transformed source
     */
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadAgreement(@RequestParam Map<String, String> parameters) {
        StreamingResponseBody streamingResponseBody = response -> {
            idsService.downloadForAgreement(response, MediaType.APPLICATION_OCTET_STREAM_VALUE, parameters);
        };
        return ResponseEntity.ok(streamingResponseBody);
    }
}
