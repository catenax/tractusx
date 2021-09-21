//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.requests;

import com.catenax.partsrelationshipservice.dtos.PartsTreeView;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Base for {@code getPartsTreeBy*} parameter objects.
 */
@AllArgsConstructor
@Getter
@SuppressWarnings({"PMD.CommentRequired", "PMD.CommentDefaultAccessModifier", "PMD.DefaultPackage"})
class PartsTreeRequestBase {
    @NotNull
    @Parameter(description = "PartsTree View to retrieve", required = true)
    PartsTreeView view;

    @Parameter(description = "Aspect information to add to the returned tree", example = "CE", schema = @Schema(implementation = String.class))
    Optional<String> aspect;

    @Parameter(description = "Max depth of the returned tree, if empty max depth is returned", schema = @Schema(implementation = Integer.class))
    Optional<Integer> depth;
}
