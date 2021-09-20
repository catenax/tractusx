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
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.net.URL;

/*** API type for aspect name/url entry. */
@Schema(description = "Aspect location data")
@Value
@Builder(toBuilder = true)
@Jacksonized
@SuppressWarnings("PMD.CommentRequired")
public class Aspect {
    @Schema(description = "Aspect name", example = "CE")
    private final String name;
    @Schema(description = "URL location of aspect data", example = "http://aspects-url/CE")
    private final URL url;
}
