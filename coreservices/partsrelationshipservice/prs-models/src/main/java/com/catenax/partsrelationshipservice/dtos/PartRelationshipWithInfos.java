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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@Schema(description = "List of the relationships with their infos")
@Builder(toBuilder = true)
@Value
public class PartRelationshipWithInfos implements Serializable {
    @Schema(description = "List of the relationships")
    List<PartRelationship> relationships;
    @Schema(description = "List of part infos")
    List<PartInfo> partInfos;
}
