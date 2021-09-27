//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.exceptions;

import net.catenax.prs.configuration.PrsConfiguration;
import net.catenax.prs.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.requests.PartsTreeByVinRequest;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Exception thrown by the service when the {@link PartsTreeByObjectIdRequest#getDepth()}
 * or {@link PartsTreeByVinRequest#getDepth()} parameter is larger than
 * {@link PrsConfiguration#getPartsTreeMaxDepth()}.
 */
@ResponseStatus(value = /* 400 */ BAD_REQUEST, reason = "Provided value for depth argument was too large")
public class MaxDepthTooLargeException extends RuntimeException {
}
