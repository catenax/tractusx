//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.brokerproxy.requests;

import com.catenax.partsrelationshipservice.dtos.PartLifecycleStage;
import com.catenax.partsrelationshipservice.dtos.PartRelationship;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/*** Request for updates to {@link PartRelationship}s. */
@Schema(description = "Describes an update of a relationship")
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartRelationshipUpdate.PartRelationshipUpdateBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartRelationshipUpdate {
    @NotNull
    @Schema(implementation = PartRelationship.class)
    private PartRelationship relationship;

    @Schema(description =
            "<ul>"
                    + "   <li>TRUE if the child is not part of the parent (used to update data, e.g. a relationship was wrongly submitted, or a part is removed from a car during maintenance)</li>"
                    + "   <li>FALSE otherwise (“normal case” - a part is added into a parent part).</li>"
                    + "</ul>")
    private boolean remove;

    @NotNull
    @Schema(description = "Whether the update applies to the time the part was built, or a maintenance operation on the part after it was built.")
    private PartLifecycleStage stage;

    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private Instant effectTime;
}
