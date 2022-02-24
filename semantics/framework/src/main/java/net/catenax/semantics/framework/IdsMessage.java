/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework;

/**
 * An Ids message
 * TODO make it generic on the payload type
 */
public interface IdsMessage {

    /**
     * @return the message body
     */
    String getPayload();

    /**
     * @return the media type of the message body
     */
    String getMediaType();

    /**
     * @return the "semantic" model of the body
     */
    String getModel();
}
