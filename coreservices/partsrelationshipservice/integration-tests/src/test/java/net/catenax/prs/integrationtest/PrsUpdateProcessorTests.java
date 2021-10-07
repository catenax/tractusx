//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.integrationtest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PrsUpdateProcessorTests extends PrsIntegrationTestsBase{

    private final PrsUpdateEventMother sampleEvents = new PrsUpdateEventMother();

    @Test
    @Disabled
    public void updatePartsAttributes_success() {
        //Arrange
        var event = sampleEvents.sampleAttributeUpdateEvent();

        //Act
        publishUpdateEvent(configuration.getPartsAttributesTopic(), event);

        //Assert
        //TODO: In API Response we have the update data.
    }
}
