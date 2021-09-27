package net.catenax.semantics.hub.bamm;

import org.apache.jena.rdf.model.Model;

import io.openmanufacturing.sds.aspectmodel.resolver.AbstractResolutionStrategy;
import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;
import io.vavr.control.Try;

public class StaticResolutionStrategy extends AbstractResolutionStrategy {
    private int counter;
    private Try<Model> model;

    public StaticResolutionStrategy(Try<Model> model) {
        this.model = model;
    }

    @Override
    public Try<Model> apply(AspectModelUrn t) {
        counter++;
        
        return this.model;
    }

    public int getResolvementCounter() {
        return counter;
    }
}
