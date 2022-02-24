/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework.config;

import lombok.Data;

/**
 * configures a transformation based on a transformation file (like a stylesheet)
 */
@Data
public class Transformation {
    /**
     * transformation stylesheet location
     */
    String file;
    /**
     * semantic model of the source of the transformation
     */
    String sourceModel;
    /**
     * media type of the source
     */
    String targetMediaType;
    /**
     * target semantic model
     */
    String targetModel;
}
