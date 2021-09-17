package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "View defining which data of the PartsTree is retrieved.")
public enum PartsTreeView {
    @Schema(description = "The view of the PartsTree as the vehicle was assembled.")
    AS_BUILT,

    @Schema(description = "The view of the PartsTree that accounts for all updates during the vehicle lifecycle.")
    AS_MAINTAINED
}