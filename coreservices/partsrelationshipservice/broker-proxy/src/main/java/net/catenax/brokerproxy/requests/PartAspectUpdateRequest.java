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

import com.catenax.partsrelationshipservice.dtos.Aspect;
import com.catenax.partsrelationshipservice.dtos.PartId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

/*** Request for updates to {@link Aspect}s. */
@Schema(description = PartAspectUpdateRequest.DESCRIPTION)
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartAspectUpdateRequest.PartAspectUpdateRequestBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartAspectUpdateRequest {
    public static final String DESCRIPTION = "Describes an update of a part aspect location.";

    @NotNull
    @Valid
    @Schema(implementation = PartId.class)
    private PartId part;

    @NotEmpty
    @Valid
    @Schema(description = "Aspect location.")
    private List<Aspect> aspects;

    @Schema(description =
            "<ul>"
                    + "   <li>TRUE if the aspect URLs are to be deleted from the part</li>"
                    + "   <li>FALSE otherwise (“normal case” - an aspect URL is added to a part).</li>"
                    + "</ul>")
    private boolean remove;

    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private Instant effectTime;
}
