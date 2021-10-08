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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * JPA entity part representing an aspect URL attached to a part identifier.
 */
@Entity
@Table(name = "part_aspect")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString // safe on this entity as it has no relationships
public class PartAspectEntity implements Serializable {

    /**
     * The entity primary key, which itself contains the part identifier
     * and aspect name.
     */
    @EmbeddedId
    private PartAspectEntityKey key;

    /**
     * The aspect URL, i.e. the URL at which aspect information on the given part
     * identifier can be retrieved.
     */
    @NotNull
    private String url;
}
