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

import javax.validation.constraints.NotNull;
import java.util.List;

/*** API type for part information retrieved alongside a parts tree. */
@Value
@Builder(toBuilder = true)
@Jacksonized
@Schema(description = "Information about parts")
@SuppressWarnings("PMD.CommentRequired")
public class PartInfo {
    @NotNull
    @Schema(description = "The part identifier.")
    private PartId part;

    @Schema(description = "Type of material, (sub)component/part or vehicle. May not be set.", example = "gearbox")
    private String partTypeName;

    @Schema(description = "List of aspect locations.")
    private List<Aspect> aspects;
}
