//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//

import lombok.Builder;
import lombok.Value;

@Value
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
    private final String view;

    /**
     * Aspect information to add to the returned tree.
     */
    private final String aspect;

    /**
     * Max depth of the returned tree, if empty max depth is returned.
     */
    private final Integer depth;
}
