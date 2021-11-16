//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Parameter object for
 * getPartsTree
 * REST operation.
 */
@Value
@Jacksonized // Makes the class deserializable using lombok builder.
@Builder(toBuilder = true)
public class PartsTreeByObjectIdRequest {

    /**
     * Readable ID of manufacturer including plant.
     */
    @NotBlank
    private String oneIDManufacturer;

    /**
     * Unique identifier of a single, unique physical (sub)component/part/batch,
     * given by its manufacturer.
     */
    @NotBlank
    private String objectIDManufacturer;

    /**
     * PartsTree View to retrieve.
     */
    @NotBlank
    private final String view;

    /**
     * Aspect information to add to the returned tree. May be {@literal null}.
     */
    private final String aspect;

    /**
     * Max depth of the returned tree, if {@literal null}, max depth is returned. May be {@literal null}.
     */
    @Min(1)
    private final Integer depth;
}
