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

import com.catenax.partsrelationshipservice.dtos.Aspect;
import com.catenax.partsrelationshipservice.dtos.PartId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

/**
 * Object representing broker messages for a PartAspectUpdateEvent.
 */
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartAspectUpdateEvent.PartAspectUpdateEventBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartAspectUpdateEvent {
    @NotNull
    @Valid
    private PartId part;

    @NotEmpty
    @Valid
    private List<Aspect> aspects;

    private boolean remove;

    @NotNull
    private Instant effectTime;
}
