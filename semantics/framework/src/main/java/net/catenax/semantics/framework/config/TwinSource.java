/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.config;

import lombok.Data;

import java.util.Map;

/**
 * A pre-configured twin source that can be published at startup
 */
@Data
public class TwinSource {
    /**
     * needs a protocol
     */
    private String protocol;
    /**
     * needs a command
     */
    private String command;
    /**
     * and a set of parameters
     */
    private Map<String,String> parameters;
}
