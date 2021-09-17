package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description = "Link between two parts.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRelationship implements Serializable {
    @NotNull
    @Schema(description = "Unique part identifier of a parent in the relationship.", implementation = PartId.class)
    private PartId parent;

    @NotNull
    @Schema(description = "Unique part identifier of a child in the relationship.", implementation = PartId.class)
    private PartId child;
}
