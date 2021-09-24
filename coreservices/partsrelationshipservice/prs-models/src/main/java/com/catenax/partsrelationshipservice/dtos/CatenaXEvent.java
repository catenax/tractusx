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

import java.time.Instant;

/**
 * Interface for events that can be dated.
 */
public interface CatenaXEvent {
    /**
     * Instant at which the event occurred.
     *
     * @return a non-{@literal null} value.
     */
    Instant getEffectTime();
}
