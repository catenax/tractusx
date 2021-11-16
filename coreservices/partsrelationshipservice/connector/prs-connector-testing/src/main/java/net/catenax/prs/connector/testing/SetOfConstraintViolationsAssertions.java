//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.testing;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.AbstractAssert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * XXX.
 */
@SuppressWarnings("PMD.LinguisticNaming")
public class SetOfConstraintViolationsAssertions extends AbstractAssert<SetOfConstraintViolationsAssertions, Set<? extends ConstraintViolation>> {

    /**
     * XXX.
     *
     * @param actual YYY
     */
    public SetOfConstraintViolationsAssertions(final Set<? extends ConstraintViolation> actual) {
        super(actual, SetOfConstraintViolationsAssertions.class);
    }

    /**
     * XX.
     *
     * @param actual XXX
     * @return XXX
     */
    public static SetOfConstraintViolationsAssertions assertThat(final Set<? extends ConstraintViolation> actual) {
        return new SetOfConstraintViolationsAssertions(actual);
    }

    /**
     * XX.
     *
     * @param path XXX
     * @return XXX
     */
    public SetOfConstraintViolationsAssertions hasViolationWithPath(final String path) {
        isNotNull();

        // check condition
        if (!containsViolationWithPath(actual, path)) {
            failWithMessage("There was no violation with path <%s>. Violation paths: <%s>", path, summary());
        }

        return this;
    }

    /**
     * XXX.
     *
     * @return XXX
     */
    public SetOfConstraintViolationsAssertions hasNoViolations() {
        isNotNull();

        if (!actual.isEmpty()) {
            failWithMessage("Expecting no violations, but there are %s violations. Violation paths: %s", actual.size(), summary());
        }

        return this;
    }

    private boolean containsViolationWithPath(final Set<? extends ConstraintViolation> violations, final String path) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(path));
    }

    private List<String> summary() {
        return actual.stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toList());
    }
}
