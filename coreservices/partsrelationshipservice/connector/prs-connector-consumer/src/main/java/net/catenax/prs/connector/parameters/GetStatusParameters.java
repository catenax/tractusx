//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.parameters;


import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.PathParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.catenax.prs.connector.consumer.controller.ConsumerApiController;

/**
 * Parameter object for {@link ConsumerApiController#getStatus(GetStatusParameters)} REST operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStatusParameters {

    /**
     * The identifier of the transfer request.
     */
    @PathParam("id")
    @NotBlank
    private String requestId;
}
