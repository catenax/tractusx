/**
 * base class for backend adapters
 */
package net.catenax.semantics.framework;

import lombok.RequiredArgsConstructor;
import net.catenax.semantics.framework.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Any adapter that is configurable
 */
@RequiredArgsConstructor
public abstract class BaseAdapter<Cmd extends Command, O extends Offer, Ct extends Catalog, Co extends Contract, T extends Transformation>
        implements IdsAdapter {

    protected final Config<Cmd,O,Ct,Co,T> configurationData;
    protected IdsConnector idsConnector;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public IdsConnector getIdsConnector() {
        return idsConnector;
    }

    @Override
    public void setIdsConnector(IdsConnector idsConnector) {
        this.idsConnector=idsConnector;
    }

}
