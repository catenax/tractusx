/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework;

import lombok.Data;

/**
 * basic implementation of an ids message
 */
@Data
public class BaseIdsMessage implements IdsMessage {
    /**
     * the actual message payload
     */
    String payload;
    /**
     * the media type of the message
     */
    String mediaType="";
    /**
     * the semantic model used in the payload
     */
    String model="";
}
