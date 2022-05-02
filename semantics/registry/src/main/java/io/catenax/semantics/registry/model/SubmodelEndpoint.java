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
package io.catenax.semantics.registry.model;

import lombok.Value;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Value
public class SubmodelEndpoint {
    @Id
    UUID id;
    String interfaceName;
    String endpointAddress;
    String endpointProtocol;
    String endpointProtocolVersion;
    String subProtocol;
    String subProtocolBody;
    String subProtocolBodyEncoding;

}
