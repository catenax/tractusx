//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static net.catenax.prs.dtos.ValidationConstants.ATTRIBUTE_MAX_LENGTH;
import static net.catenax.prs.dtos.ValidationConstants.ATTRIBUTE_MIN_LENGTH;

/*** API type for aspect name/url entry. */
@Schema(description = "Aspect location data")
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = Aspect.AspectBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class Aspect {

    @NotBlank
    @Size(min = ATTRIBUTE_MIN_LENGTH, max = ATTRIBUTE_MAX_LENGTH)
    @Schema(description = "Aspect name", example = "CE", minLength = ATTRIBUTE_MIN_LENGTH, maxLength =  ATTRIBUTE_MAX_LENGTH)
    private String name;

    @NotBlank
    @URL
    @Size(min = ATTRIBUTE_MIN_LENGTH, max = ATTRIBUTE_MAX_LENGTH)
    @Schema(description = "URL location of aspect data", minLength = ATTRIBUTE_MIN_LENGTH, maxLength =  ATTRIBUTE_MAX_LENGTH, example = "http://aspects-url/CE", implementation = java.net.URL.class)
    private String url;
}
