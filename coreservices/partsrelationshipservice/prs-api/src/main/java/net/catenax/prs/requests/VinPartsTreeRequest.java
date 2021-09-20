//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.requests;

import com.catenax.partsrelationshipservice.dtos.PartsTreeView;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

/**
 * Parameter object for getPartsTreeByVin REST operation.
 */
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@SuppressWarnings("PMD.CommentRequired")
public class VinPartsTreeRequest {
    @Parameter(description = "Vehicle Identification Number", in = PATH, required = true)
    private final String vin;

    @Parameter(description = "PartsTree View to retrieve", required = true)
    private final PartsTreeView view;

    @Parameter(description = "Aspect information to add to the returned tree", example = "CE")
    private final Optional<String> aspect = Optional.empty();

    @Parameter(description = "Max depth of the returned tree, if empty max depth is returned")
    private final Optional<Integer> depth = Optional.empty();
}
