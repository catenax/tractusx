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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static net.catenax.prs.dtos.ValidationConstants.ATTRIBUTE_MAX_LENGTH;
import static net.catenax.prs.dtos.ValidationConstants.ATTRIBUTE_MIN_LENGTH;

/*** API type for a part identifier. */
@Schema(description = "Unique part identifier")
@Value
@Builder(toBuilder = true, setterPrefix = "with")
@JsonDeserialize(builder = PartId.PartIdBuilder.class)
@SuppressWarnings("PMD.CommentRequired")
public class PartId {

    @NotBlank
    @Size(min = ATTRIBUTE_MIN_LENGTH, max = ATTRIBUTE_MAX_LENGTH)
    @Schema(description = "Readable ID of manufacturer including plant", minLength = ATTRIBUTE_MIN_LENGTH, maxLength =  ATTRIBUTE_MAX_LENGTH)
    private String oneIDManufacturer;

    @NotBlank
    @Size(min = ATTRIBUTE_MIN_LENGTH, max = ATTRIBUTE_MAX_LENGTH)
    @Schema(description = "Unique identifier of a single, unique physical (sub)component/part/batch, given by its manufacturer. For a vehicle, the Vehicle Identification Number (VIN).", minLength = ATTRIBUTE_MIN_LENGTH, maxLength =  ATTRIBUTE_MAX_LENGTH)
    private String objectIDManufacturer;
}
