//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * JPA entity part representing the primary key of the {@link PartRelationshipEntity}.
 */
@Embeddable
@Data // safe on this class as it is not an @Entity, and it has no JPA relationships
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartRelationshipEntityKey implements Serializable {

    /**
     * A value linking {@link PartRelationshipEntity} tuples originating in the same uploaded message.
     */
    @NotNull
    private UUID partRelationshipListId;

    /**
     * Part identifier of the parent in the relationship.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "oneIDManufacturer", column = @Column(name = "parentOneIDManufacturer")),
        @AttributeOverride(name = "objectIDManufacturer", column = @Column(name = "parentObjectIDManufacturer")),
    })
    @NotNull
    private PartIdEntityPart parentId;

    /**
     * Part identifier of the child in the relationship.
     */
    @Embedded
    @NotNull
    private PartIdEntityPart childId;
}
