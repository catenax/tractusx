/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.config;

import java.util.Map;

import lombok.Data;

/**
 * Represents a backend "command" for adaption
 */
@Data
public class Command {
    /**
     * original "semantic" model spoken in the command
     */
    private String model="";
    /**
     * some mapping of parameters to views
     */
    private Map<String,String> aliases;
    /**
     * a file associated with the backend
     */
    private String file;
    /**
     * a database associated with the backend
     */
    private DataSource datasource;
}
