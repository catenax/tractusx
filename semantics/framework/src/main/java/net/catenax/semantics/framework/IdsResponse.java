/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework;

import java.util.concurrent.Future;

/**
 * an immediate (synchronous) response that is returned by the IDS control
 * layer and which is the interface to an eventually asynchronous data flow
 */
public interface IdsResponse {
    /**
     * @return the "future" message (in case of asynchronous calls)
     */
    public Future<IdsMessage> getMessage();

    /**
     * return the current status of the request
     * @return 0 if the request is ongoing, any other code if the request has been finished
     */
    public int getStatus();
}
