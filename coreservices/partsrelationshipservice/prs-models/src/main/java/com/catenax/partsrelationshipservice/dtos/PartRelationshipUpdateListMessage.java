//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package com.catenax.partsrelationshipservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 *
 */
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
