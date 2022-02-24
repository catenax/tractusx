/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.framework;

/**
 * An adapter is a facility which translated between an external API
 * (internal plane API, data plane API) and the IDS connector plane
 */
public interface IdsAdapter {

    /**
     * @return the associated ids service
     */
    public IdsConnector getIdsConnector();

    /**
     * registers the associated ids service
     * (not autowirable because of circular dependencies)
     * @param connector
     */
    public void setIdsConnector(IdsConnector connector);
}
