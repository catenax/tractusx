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
import java.time.Instant;

/*** API type for a single part type name update event. */
@Value
@Builder(toBuilder = true)
@Jacksonized
@Schema(description = PartTypeNameUpdate.DESCRIPTION)
@SuppressWarnings("PMD.CommentRequired")
public class PartTypeNameUpdate implements CatenaXEvent {
    public static final String DESCRIPTION = "Describes an update of a part type name.";

    @Schema(implementation = PartId.class)
    private final PartId part;

    @Schema(description = "Type of material, (sub)component/part or vehicle", example = "gearbox")
    private final String partTypeName;
    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private final Instant effectTime;
}
