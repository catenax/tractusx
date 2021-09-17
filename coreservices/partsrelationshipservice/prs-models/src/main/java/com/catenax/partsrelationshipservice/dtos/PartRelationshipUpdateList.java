package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Schema(description = PartRelationshipUpdateList.DESCRIPTION)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRelationshipUpdateList implements Serializable {
    public static final String DESCRIPTION = "Describes an update of (part of) a BOM.";

    @Schema(description = "List of relationships updates")
    private List<PartRelationshipUpdate> relationships;
}
