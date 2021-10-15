//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.dtos.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import net.catenax.prs.annotations.ValueOfEnum;
import net.catenax.prs.dtos.PartAttribute;
import net.catenax.prs.dtos.PartId;
import net.catenax.prs.dtos.PartInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/*** Request for updates to {@link PartInfo}s. */
@Schema(description = PartAttributeUpdateRequest.DESCRIPTION)
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartAttributeUpdateRequest.PartAttributeUpdateRequestBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartAttributeUpdateRequest {
    public static final String DESCRIPTION = "Describes an update of a part attribute.";

    @NotNull
    @Valid
    @Schema(implementation = PartId.class)
    private PartId part;

    @NotNull
    @ValueOfEnum(enumClass = PartAttribute.class, message = "Invalid attribute name.")
    @Schema(implementation = PartAttribute.class, description = "Attribute name")
    private String name;

    @Schema(description = "Attribute value", example = "Vehicle")
    @NotNull
    private String value;

    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private Instant effectTime;
}
