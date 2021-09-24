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

/***
 * API type for the stage at which an event occurred, determining which events are used
 * to generate a particular {@link PartsTreeView}.
 *
 * @see PartsTreeView
 */
@Schema(description = "Stage defining whether changes apply to the AS_BUILT or AS_MAINTAINED BOM views.")
@SuppressWarnings("PMD.CommentRequired")
public enum PartLifecycleStage {
    @Schema(description = "The time the part is built.")
    BUILD,

    @Schema(description = "The time after the part is built.")
    MAINTENANCE
}
