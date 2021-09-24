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

import java.util.List;

/*** Message type for a list of {@link PartRelationshipUpdate}s. */
@Schema(description = PartRelationshipUpdateList.DESCRIPTION)
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartRelationshipUpdateList.PartRelationshipUpdateListBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartRelationshipUpdateList {
    public static final String DESCRIPTION = "Describes an update of (part of) a BOM.";

    @Schema(description = "List of relationships updates")
    private List<PartRelationshipUpdate> relationships;
}
