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

package io.catenax.semantics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import io.catenax.semantics.hub.persistence.PersistenceLayer;

@Component
public class TriplestoreLivenessProbe implements HealthIndicator {
    private PersistenceLayer pl;

    public TriplestoreLivenessProbe(PersistenceLayer pl) {
        this.pl = pl;
    }

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Health health() {
        try {
            if(pl.echo()) {
                return Health.up().build();
            }
            return Health.down().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Health.down().build();
        }
    }
}
