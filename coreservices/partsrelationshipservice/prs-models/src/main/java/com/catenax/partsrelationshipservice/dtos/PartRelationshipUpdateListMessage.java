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

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/*** API type. */
@Value
@Builder(toBuilder = true)
@Jacksonized
@SuppressWarnings("PMD.CommentRequired")
public class PartRelationshipUpdateListMessage {
    @NotNull
    private final UUID partRelationshipStatusListId;

    @NotNull
    private final PartRelationshipUpdateList payload;

    @NotNull
    private final Instant uploadDateTime;
}
