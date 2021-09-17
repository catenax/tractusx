package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Schema(description = "Unique part identifier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartId implements Serializable {
    @NotBlank
    @Schema(description = "Readable ID of manufacturer including plant", example = "BMW MUC")
    private String oneIDManufacturer;

    @Schema(description = "Unique identifier of a single, unique physical (sub)component/part/batch, given by its manufacturer. For a vehicle, the Vehicle Identification Number (VIN).", example = "1122334455")
    private String objectIDManufacturer;
}
