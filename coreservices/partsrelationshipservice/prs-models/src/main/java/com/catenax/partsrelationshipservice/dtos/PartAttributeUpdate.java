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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/*** Message type for updates to {@link PartInfo}s. */
@Schema(description = PartAttributeUpdate.DESCRIPTION)
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartAttributeUpdate.PartAttributeUpdateBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartAttributeUpdate implements CatenaXEvent {
    public static final String DESCRIPTION = "Describes an update of a part attribute.";

    @NotNull
    @Valid
    @Schema(implementation = PartId.class)
    private PartId part;

    @NotNull
    @Schema(description = "Attribute name")
    private PartAttributeName name;

    @Schema(description = "Attribute value", example = "Vehicle")
    private String value;

    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private Instant effectTime;


//    public PartAttributeName getName() {
//        return name;
//    }
}
