/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework;

import lombok.Data;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of an already resolved message future
 */
@Data
public class NowFutureIdsMessage implements Future<IdsMessage> {
    IdsMessage message;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public IdsMessage get()  {
        return message;
    }

    @Override
    public IdsMessage get(long timeout, TimeUnit unit)  {
        return message;
    }
}
