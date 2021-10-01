//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package com.catenax.partsrelationshipservice.dtos.messaging;

import com.catenax.partsrelationshipservice.dtos.PartAttributeName;
import com.catenax.partsrelationshipservice.dtos.PartId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * Object representing broker messages for a PartAttributeUpdateEvent.
 */
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartAttributeUpdateEvent.PartAttributeUpdateEventBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartAttributeUpdateEvent {
    @NotNull
    @Valid
    private PartId part;

    @NotNull
    private PartAttributeName name;

    @NotNull
    private String value;

    @NotNull
    private Instant effectTime;
}
