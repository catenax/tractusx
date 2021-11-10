/*
 * Copyright (c) 2021 Robert Bosch Manufacturing Solutions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.catenax.semantics.hub.persistence;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import io.vavr.control.Try;
import net.catenax.semantics.hub.model.Model;
import net.catenax.semantics.hub.model.NewModel;

public interface PersistenceLayer {
    public List<Model> getModels(@Nullable Boolean isPrivate, String namespaceFilter, String nameFilter, @Nullable String type, int page, int pageSize);

    public Model getModel(String modelId);

    public Optional<Model> insertNewModel(NewModel model, String id, String version, String name);

    public Optional<String> getModelDefinition(String modelId);

    public Try<Void> deleteModel(String modelId);

    public Optional<Model> updateExistingModel(NewModel model, String id, String version, String name);
}