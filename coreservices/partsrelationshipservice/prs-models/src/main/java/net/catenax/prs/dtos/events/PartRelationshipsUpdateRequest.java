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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/*** Request for a list of {@link PartRelationshipUpdate}s. */
@Schema(description = PartRelationshipsUpdateRequest.DESCRIPTION)
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartRelationshipsUpdateRequest.PartRelationshipsUpdateRequestBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartRelationshipsUpdateRequest {
    public static final String DESCRIPTION = "Describes an update of (part of) a BOM.";

    @Valid
    @NotEmpty
    @Schema(description = "List of relationships updates")
    private List<PartRelationshipUpdate> relationships;
}
