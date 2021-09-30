//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerproxy.controllers;

import com.catenax.partsrelationshipservice.dtos.PartAspectUpdate;
import com.catenax.partsrelationshipservice.dtos.PartAttributeUpdate;
import com.catenax.partsrelationshipservice.dtos.PartRelationshipUpdateList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.BrokerProxyApplication;
import net.catenax.brokerproxy.annotations.ExcludeFromCodeCoverageGeneratedReport;
import net.catenax.brokerproxy.services.BrokerProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;


import javax.validation.Valid;



/**
 * Broker proxy REST controller.
 */
@Tag(name = "Broker HTTP Proxy API")
@Slf4j
@RestController
@RequestMapping(BrokerProxyApplication.API_PREFIX)
@RequiredArgsConstructor
@ExcludeFromCodeCoverageGeneratedReport
@SuppressWarnings({"checkstyle:MissingJavadocMethod", "PMD.CommentRequired"})
public class BrokerProxyController {

    private final BrokerProxyService brokerProxyService;

    @Operation(operationId = "uploadPartRelationshipUpdateList",
         summary = "Upload a PartRelationshipUpdateList. " + PartRelationshipUpdateList.DESCRIPTION)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" /* no content */,
            description = "PartRelationshipUpdateList uploaded successfully"),
    })
    @PostMapping("/PartRelationshipUpdateList")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPartRelationshipUpdateList(final @RequestBody @Valid PartRelationshipUpdateList data) {
        brokerProxyService.send(data);
    }

    @Operation(operationId = "uploadPartAspectUpdate",
        summary = "Upload a PartAspectUpdate. " + PartAspectUpdate.DESCRIPTION)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" /* no content */,
            description = "PartAspectUpdate uploaded successfully"),
    })
    @PostMapping("/PartAspectUpdate")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPartAspectUpdate(final @RequestBody @Valid PartAspectUpdate data) {
        brokerProxyService.send(data);
    }

    @Operation(operationId = "uploadPartAttributeUpdate",
        summary = "Upload a PartAttributeUpdate. " + PartAttributeUpdate.DESCRIPTION)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" /* no content */,
            description = "PartAttributeUpdate uploaded successfully"),
    })
    @PostMapping("/PartAttributeUpdate")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPartAttributeUpdate(final @RequestBody @Valid PartAttributeUpdate data) {
        brokerProxyService.send(data);
    }
}
