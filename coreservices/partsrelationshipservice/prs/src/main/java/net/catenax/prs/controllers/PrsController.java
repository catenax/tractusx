//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.controllers;

import com.catenax.partsrelationshipservice.dtos.ErrorResponse;
import com.catenax.partsrelationshipservice.dtos.PartRelationshipWithInfos;
import com.catenax.partsrelationshipservice.dtos.PartsTreeView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.prs.util.SampleData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Application REST controller.
 */
@Tag(name = "Parts Relationship Service API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class PrsController {

    @Operation(summary = "Get a PartsTree for a VIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the PartsTree",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PartRelationshipWithInfos.class))}),
            @ApiResponse(responseCode = "404", description = "PartsTree not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("api/v0.1/vins/{vin}/partsTree")
    public PartRelationshipWithInfos getPartsTree(
            @Parameter(description = "Vehicle Identification Number", example = SampleData.SERIAL_NUMBER_BMW_PART_1) @PathVariable String vin,
            @Parameter(description = "PartsTree View to retrieve") @RequestParam PartsTreeView view,
            @Parameter(description = "Aspect information to add to the returned tree", example = "CE") @RequestParam Optional<String> aspect,
            @Parameter(description = "Max depth of the returned tree, if empty max depth is returned") @RequestParam Optional<Integer> depth) throws Exception {
        var objectMapper =new ObjectMapper();
        return objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("response_1631610272167.json"), PartRelationshipWithInfos.class);
    }
}
