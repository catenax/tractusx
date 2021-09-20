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

/***
 * An event that can be assigned an occurrence time.
 */
public interface CatenaXEvent {
    /**
     *
     * @return the time the event occurred.
     */
    Instant getEffectTime();
}
