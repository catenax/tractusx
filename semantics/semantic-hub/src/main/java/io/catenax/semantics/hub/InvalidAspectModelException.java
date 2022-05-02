/********************************************************************************
 * Copyright (c) 2021-2022 Robert Bosch Manufacturing Solutions GmbH
 *
 * See the AUTHORS file(s) distributed with this work for additional
 * information regarding authorship.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 * 
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package io.catenax.semantics.hub;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class InvalidAspectModelException extends RuntimeException {

   private static final long serialVersionUID = 3238030351289237184L;
   private static final String MESSAGE_VALIDATION_FAILED = "Validation failed";
   public static final String DETAILS_KEY = "validationError";

   private final Map<String, String> details;

   public InvalidAspectModelException( final String detail ) {
      super( MESSAGE_VALIDATION_FAILED );
      details = new LinkedHashMap<>();
      details.put( DETAILS_KEY, detail );
   }

   public InvalidAspectModelException( final Map<String, String> details ) {
      this( MESSAGE_VALIDATION_FAILED, details );
   }

   private InvalidAspectModelException( final String message, final Map<String, String> details ) {
      super( message );
      if ( Objects.isNull( details ) ) {
         this.details = new LinkedHashMap<>();
      } else {
         this.details = details;
      }
   }

   public Map<String, String> getDetails() {
      return details;
   }
}
