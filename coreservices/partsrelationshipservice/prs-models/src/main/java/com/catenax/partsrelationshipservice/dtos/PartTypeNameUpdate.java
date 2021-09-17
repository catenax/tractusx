package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = PartTypeNameUpdate.DESCRIPTION)
public class PartTypeNameUpdate extends BaseUpdate {
    public static final String DESCRIPTION = "Describes an update of a part type name.";

    @Schema(implementation = PartId.class)
    PartId part;

    @Schema(description = "Type of material, (sub)component/part or vehicle", example = "gearbox")
    String partTypeName;
}
