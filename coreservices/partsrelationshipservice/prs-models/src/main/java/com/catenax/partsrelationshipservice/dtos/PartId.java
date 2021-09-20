//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

/*** API type for a part identifier. */
@Schema(description = "Unique part identifier")
@Value
@Builder(toBuilder = true)
@Jacksonized
@SuppressWarnings("PMD.CommentRequired")
public class PartId {
    @NotBlank
    @Schema(description = "Readable ID of manufacturer including plant", example = "BMW MUC")
    private final String oneIDManufacturer;

    @Schema(description = "Unique identifier of a single, unique physical (sub)component/part/batch, given by its manufacturer. For a vehicle, the Vehicle Identification Number (VIN).", example = "1122334455")
    private final String objectIDManufacturer;
}
