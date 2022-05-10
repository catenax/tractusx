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

package org.eclipse.tractusx.semantics.hub.persistence;

import javax.annotation.Nullable;

import org.eclipse.tractusx.semantics.hub.domain.ModelPackageStatus;
import org.eclipse.tractusx.semantics.hub.domain.ModelPackageUrn;
import org.eclipse.tractusx.semantics.hub.model.SemanticModel;
import org.eclipse.tractusx.semantics.hub.model.SemanticModelList;
import org.eclipse.tractusx.semantics.hub.model.SemanticModelStatus;
import org.eclipse.tractusx.semantics.hub.model.SemanticModelType;

import io.openmanufacturing.sds.aspectmodel.urn.AspectModelUrn;

/**
 * Interface to any model persistency implementation
 */
public interface PersistenceLayer {
   /**
    * search a list of persisted models based on a set of mandatory and optional parameters
    *
    * @param namespaceFilter substring flag
    * @param nameFilter substring flag
    * @param nameType optional string flag determining the scope of the nameFilter (default: the model name
    *       _NAME_, but maybe refer any RDF object)
    * @param status optional string flag
    * @param page number of the page to deliver
    * @param pageSize size of the pages to batch the results in
    * @return a list of models belonging to the searched page
    */
   SemanticModelList getModels(String namespaceFilter, String nameFilter,
                               @Nullable String nameType, @Nullable ModelPackageStatus status, Integer page, Integer pageSize );

   SemanticModel getModel(AspectModelUrn urn );

   SemanticModel save(SemanticModelType type, String newModel, SemanticModelStatus status);

   String getModelDefinition( AspectModelUrn urn );

   void deleteModelsPackage( ModelPackageUrn urn );

   boolean echo();
}
