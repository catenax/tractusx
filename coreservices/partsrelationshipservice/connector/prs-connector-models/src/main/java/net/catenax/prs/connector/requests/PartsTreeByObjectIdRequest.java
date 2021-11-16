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
    private String oneIDManufacturer;

    /**
     * Unique identifier of a single, unique physical (sub)component/part/batch,
     * given by its manufacturer.
     */
    private String objectIDManufacturer;

    /**
     * PartsTree View to retrieve.
     */
    protected final String view;

    /**
     * Aspect information to add to the returned tree.
     */
    protected final String aspect;

    /**
     * Max depth of the returned tree, if empty max depth is returned.
     */
    protected final Integer depth;
}
