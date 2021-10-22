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


import net.catenax.prs.annotations.ExcludeFromCodeCoverageGeneratedReport;

/**
 * Helper class contains dto attributes validations as reusable constants.
 */
@ExcludeFromCodeCoverageGeneratedReport
public class ValidationConstants {
    /**
     * Minimum length limit for an input attribute.
     */
    public static final int ATTRIBUTE_MIN_LENGTH = 1;
    /**
     * Maximum length limit for an input attribute.
     * 10000 is chosen as a high enough value to protect api against very large inputs.
     */
    public static final int ATTRIBUTE_MAX_LENGTH = 10_000;
}
