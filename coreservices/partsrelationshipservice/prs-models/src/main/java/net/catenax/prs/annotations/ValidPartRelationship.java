//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.annotations;

import net.catenax.prs.validators.PartRelationshipValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation to validate {@link net.catenax.prs.dtos.PartRelationship} input.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = PartRelationshipValidator.class)
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public @interface ValidPartRelationship {
    String message() default "Invalid part relationship";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
