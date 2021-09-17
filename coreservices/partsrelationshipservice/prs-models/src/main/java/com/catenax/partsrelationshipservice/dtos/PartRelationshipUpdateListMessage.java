package com.catenax.partsrelationshipservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRelationshipUpdateListMessage implements Serializable {
    @NotNull
    private UUID partRelationshipStatusListId;

    @NotNull
    private PartRelationshipUpdateList payload;

    @NotNull
    private Instant uploadDateTime;
}
