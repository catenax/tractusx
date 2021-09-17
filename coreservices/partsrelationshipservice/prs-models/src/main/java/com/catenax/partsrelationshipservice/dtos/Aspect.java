//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package com.catenax.partsrelationshipservice.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.net.URL;

/**
 *
 */
@Schema(description = "Aspect location data")
@Value
public class Aspect {
    @Schema(description = "Aspect name", example = "CE")
    String name;
    @Schema(description = "URL location of aspect data", example = "http://aspects-url/CE")
    URL url;
}
