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

import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.PartRelationship;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;


/**
 * Object representing broker messages for a PartRelationshipUpdateEvent.
 */
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartRelationshipUpdateEvent.PartRelationshipUpdateEventBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartRelationshipUpdateEvent {

    @Valid
    @NotEmpty
    private List<RelationshipUpdate> relationships;

    /**
     * Representing each relationship within update event.
     */
    @Value
    @Builder(toBuilder = true, setterPrefix = "with")
    @JsonDeserialize(builder = RelationshipUpdate.RelationshipUpdateBuilder.class)
    public static class RelationshipUpdate {
        @NotNull
        private PartRelationship relationship;

        private boolean remove;

        @NotNull
        private PartLifecycleStage stage;

        @NotNull
        private Instant effectTime;
    }
}
