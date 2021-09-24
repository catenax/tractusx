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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/*** Message type for updates to {@link PartInfo}s. */
@Schema(description = PartTypeNameUpdate.DESCRIPTION)
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartTypeNameUpdate.PartTypeNameUpdateBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartTypeNameUpdate implements CatenaXEvent {
    public static final String DESCRIPTION = "Describes an update of a part type name.";

    @Schema(implementation = PartId.class)
    private PartId part;

    @Schema(description = "Type of material, (sub)component/part or vehicle", example = "gearbox")
    private String partTypeName;

    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private Instant effectTime;
}
