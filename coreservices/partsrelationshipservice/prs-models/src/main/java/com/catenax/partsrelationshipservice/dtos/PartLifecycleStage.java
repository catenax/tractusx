package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stage defining whether changes apply to the AS_BUILT or AS_MAINTAINED BOM views.")
public enum PartLifecycleStage {
    @Schema(description = "The time the part is built.")
    BUILD,

    @Schema(description = "The time after the part is built.")
    MAINTENANCE
}