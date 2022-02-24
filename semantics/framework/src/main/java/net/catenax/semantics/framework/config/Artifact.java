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
import java.util.UUID;

/**
 * Represents an artifact
 */
@Data
public class Artifact {
    /**
     * id of the artifact (once registered or to be registered)
     */
    private UUID id;
    /**
     * complete uri of the artifact (once registered)
     */
    private String uri;
    /**
     * description of the artifact
     */
    private String description;
    /**
     * has a link to a "protocol" or backend
     */
    private String protocol;
    /**
     * has a link to a "command" or action
     */
    private String command;

}
