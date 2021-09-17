package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.List;

@Value
@Schema(description = "Information about parts")
public class PartInfo {
    @Schema(implementation = PartId.class)
    private PartId part;

    @Schema(description = "Type of material, (sub)component/part or vehicle", example = "gearbox")
    private String partTypeName;

    @Schema(description = "List of aspect locations.")
    private List<Aspect> aspects;
}
