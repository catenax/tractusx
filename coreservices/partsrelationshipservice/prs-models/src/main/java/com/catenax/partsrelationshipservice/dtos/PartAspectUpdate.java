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

import com.google.common.collect.ImmutableList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

/*** API type for a single part aspect update event. */
@Value
@Builder(toBuilder = true)
@Jacksonized
@Schema(description = PartAspectUpdate.DESCRIPTION)
@SuppressWarnings("PMD.CommentRequired")
public class PartAspectUpdate implements CatenaXEvent {
    public static final String DESCRIPTION = "Describes an update of a part aspect location.";

    @Schema(implementation = PartId.class)
    private final PartId part;

    @NotEmpty
    @Schema(description = "Aspect location.")
    private final ImmutableList<Aspect> aspects;

    @Schema(description =
            "<ul>"
                    + "   <li>TRUE if the aspect URLs are to be deleted from the part</li>"
                    + "   <li>FALSE otherwise (“normal case” - an aspect URL is added to a part).</li>"
                    + "</ul>")
    private final boolean remove;
    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private final Instant effectTime;
}
