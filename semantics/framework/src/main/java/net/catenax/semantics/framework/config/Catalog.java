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
 * represents a catalog configuration
 */
@Data
public class Catalog {
    private UUID id;
    private String uri;
    private String description;
}
