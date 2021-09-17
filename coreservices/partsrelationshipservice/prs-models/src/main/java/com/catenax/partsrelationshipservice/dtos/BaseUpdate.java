package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class BaseUpdate implements Serializable {
    @Schema(description = "Instant at which the update was applied")
    @NotNull
    private Instant effectTime;
}
