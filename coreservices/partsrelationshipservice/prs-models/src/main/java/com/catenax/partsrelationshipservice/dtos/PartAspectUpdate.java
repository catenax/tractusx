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
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/***
 *
 */
@Value
@Schema(description = PartAspectUpdate.DESCRIPTION)
public class PartAspectUpdate extends BaseUpdate {
    public static final String DESCRIPTION = "Describes an update of a part aspect location.";

    @Schema(implementation = PartId.class)
    PartId part;

    @NotEmpty
    @Schema(description = "Aspect location.")
    List<Aspect> aspects;

    @Schema(description =
            "<ul>"
                    + "   <li>TRUE if the aspect URLs are to be deleted from the part</li>"
                    + "   <li>FALSE otherwise (“normal case” - an aspect URL is added to a part).</li>"
                    + "</ul>")
    private boolean remove = false;
}
