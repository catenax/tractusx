/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework;

/**
 * interface to any transformation facility
 */
public interface Transformer {

    /**
     * whether this transformator can handle the given transformation
     * @param incoming ids message
     * @param request to satisfy (semicolon separated)
     * @param targetModel to satisfy
     * @return boolean
     */
    public boolean canHandle(IdsMessage incoming, IdsRequest request, String targetModel);

    /**
     * transform a given message body
     * @param incoming ids message
     * @param request to satisfy
     * @param targetModel to satisfy
     * @return transformed message that satisfies the request
     * @throws StatusException
     */
    public IdsMessage transform(IdsMessage incoming, IdsRequest request, String targetModel) throws StatusException;

}
