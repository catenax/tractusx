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

import java.util.List;

/*** API type for a collection of {@link PartRelationshipUpdate} items. */
@Schema(description = PartRelationshipUpdateList.DESCRIPTION)
@Value
@Builder(toBuilder = true)
@Jacksonized
@SuppressWarnings("PMD.CommentRequired")
public class PartRelationshipUpdateList {
    public static final String DESCRIPTION = "Describes an update of (part of) a BOM.";

    @Schema(description = "List of relationships updates")
    private final List<PartRelationshipUpdate> relationships;
}
