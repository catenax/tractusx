package net.catenax.semantics.hub.persistence;

import java.util.Optional;

import io.vavr.control.Try;
import net.catenax.semantics.hub.model.Model;
import net.catenax.semantics.hub.model.ModelList;
import net.catenax.semantics.hub.model.NewModel;

public class TripleStorePersistence implements PersistenceLayer {

    @Override
    public ModelList getModels(Boolean isPrivate, String namespaceFilter, String nameFilter, String nameType,
            String type, String status, int page, int pageSize) {
        return null;
    }

    @Override
    public Model getModel(String modelId) {
        return null;
    }

    @Override
    public Optional<Model> insertNewModel(NewModel model, String id, String version, String name) {
        return null;
    }

    @Override
    public Optional<String> getModelDefinition(String modelId) {
        return null;
    }

    @Override
    public Try<Void> deleteModel(String modelId) {
        return null;
    }

    @Override
    public Optional<Model> updateExistingModel(NewModel model, String id, String version, String name) {
        return null;
    }


    
}
