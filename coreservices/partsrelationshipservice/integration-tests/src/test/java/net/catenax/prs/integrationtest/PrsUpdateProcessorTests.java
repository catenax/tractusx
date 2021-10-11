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

import com.catenax.partsrelationshipservice.dtos.messaging.EventCategory;
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
        publishUpdateEvent(EventCategory.PARTS_ATTRIBUTE, event);

        //Assert
        //TODO: Wait for data to be processed and then in API Response we should have the update data.
    }
}
