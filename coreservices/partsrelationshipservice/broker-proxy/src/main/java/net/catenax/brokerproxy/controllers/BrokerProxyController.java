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

import com.catenax.partsrelationshipservice.dtos.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.brokerproxy.BrokerProxyApplication;
import net.catenax.brokerproxy.annotations.ExcludeFromCodeCoverageGeneratedReport;
import net.catenax.brokerproxy.requests.PartAspectUpdateRequest;
import net.catenax.brokerproxy.requests.PartAttributeUpdateRequest;
import net.catenax.brokerproxy.requests.PartRelationshipUpdateRequest;
import net.catenax.brokerproxy.services.BrokerProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * Broker proxy REST controller.
 */
@Tag(name = "Broker HTTP Proxy API")
@Slf4j
@RestController
@RequestMapping(BrokerProxyApplication.API_PREFIX)
@RequiredArgsConstructor
@ExcludeFromCodeCoverageGeneratedReport
@SuppressWarnings({"checkstyle:MissingJavadocMethod", "PMD.CommentRequired", "PMD.UnnecessaryAnnotationValueElement"})
public class BrokerProxyController {

    private final BrokerProxyService brokerProxyService;

    @Operation(operationId = "uploadPartRelationshipUpdateList",
         summary = "Upload a PartRelationshipUpdateList. " + PartRelationshipUpdateRequest.DESCRIPTION)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" /* no content */,
            description = "PartRelationshipUpdateList uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/partRelationshipUpdateList")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPartRelationshipUpdateList(final @RequestBody @Valid PartRelationshipUpdateRequest data) {
        brokerProxyService.send(data);
    }

    @Operation(operationId = "uploadPartAspectUpdate",
        summary = "Upload a PartAspectUpdate. " + PartAspectUpdateRequest.DESCRIPTION)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" /* no content */,
            description = "PartAspectUpdate uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/partAspectUpdate")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPartAspectUpdate(final @RequestBody @Valid PartAspectUpdateRequest data) {
        brokerProxyService.send(data);
    }

    @Operation(operationId = "uploadPartAttributeUpdate",
        summary = "Upload a PartAttributeUpdate. " + PartAttributeUpdateRequest.DESCRIPTION)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" /* no content */,
            description = "PartAttributeUpdate uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/partAttributeUpdate")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void uploadPartAttributeUpdate(final @RequestBody @Valid PartAttributeUpdateRequest data) {
        brokerProxyService.send(data);
    }
}
