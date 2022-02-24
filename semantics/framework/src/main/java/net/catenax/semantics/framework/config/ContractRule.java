/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.config;

import java.util.UUID;

import lombok.Data;

/**
 * represents a contract rule, such as a constrained/actioned permission
 */
@Data
public class ContractRule {
    private UUID id;
    private String uri;
    private String description;
    /**
     * hosts the actual rule definition as a json string
     */
    private String value;
}
