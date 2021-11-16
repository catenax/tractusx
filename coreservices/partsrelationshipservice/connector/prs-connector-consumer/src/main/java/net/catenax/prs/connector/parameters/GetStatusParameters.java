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

/**
 * XXX.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStatusParameters {

    /**
     * XXX.
     */
    @PathParam("id")
    @NotBlank
    private String requestId;
}
