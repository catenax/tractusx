/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.restapi.dto;

import java.util.Map;
import java.util.UUID;

import lombok.Data;

@Data
public class Source {
    private UUID id;
    private String uri;
    private String description;
    private String file;
    private String transformation;
    private String type;
    private Map<String,String> aliases;
    private DataSource datasource;
}
