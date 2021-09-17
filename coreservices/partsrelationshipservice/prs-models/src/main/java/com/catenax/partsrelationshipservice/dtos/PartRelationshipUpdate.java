package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description = "Describes an update of a relationship")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRelationshipUpdate extends BaseUpdate {
    @NotNull
    @Schema(implementation = PartRelationship.class)
    private PartRelationship relationship;

    @Schema(description =
            "<ul>" +
            "   <li>TRUE if the child is not part of the parent (used to update data, e.g. a relationship was wrongly submitted, or a part is removed from a car during maintenance)</li>" +
            "   <li>FALSE otherwise (“normal case” - a part is added into a parent part).</li>" +
            "</ul>")
    private boolean remove = false;

    @NotNull
    @Schema(description = "Whether the update applies to the time the part was built, or a maintenance operation on the part after it was built.")
    private PartLifecycleStage stage;
}
