/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.restapi.dto;

import lombok.Data;

@Data
public class DataSource {
    private String driverClassName;
    private String password;
    private String username;
    private String url;
}