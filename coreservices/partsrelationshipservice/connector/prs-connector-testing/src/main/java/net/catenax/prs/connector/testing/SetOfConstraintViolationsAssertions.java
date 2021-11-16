package net.catenax.prs.connector.testing;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.AbstractAssert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SetOfConstraintViolationsAssertions extends AbstractAssert<SetOfConstraintViolationsAssertions, Set<? extends ConstraintViolation>> {

    public SetOfConstraintViolationsAssertions(Set<? extends ConstraintViolation> actual) {
        super(actual, SetOfConstraintViolationsAssertions.class);
    }

    public static SetOfConstraintViolationsAssertions assertThat(Set<? extends ConstraintViolation> actual) {
        return new SetOfConstraintViolationsAssertions(actual);
    }

    public SetOfConstraintViolationsAssertions hasViolationWithPath(String path) {
        isNotNull();

        // check condition
        if (!containsViolationWithPath(actual, path)) {
            failWithMessage("There was no violation with path <%s>. Violation paths: <%s>", path, summary());
        }

        return this;
    }

    private List<String> summary() {
        return actual.stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toList());
    }

    public SetOfConstraintViolationsAssertions hasNoViolations() {
        isNotNull();

        if (!actual.isEmpty()) {
            failWithMessage("Expecting no violations, but there are %s violations. Violation paths: %s", actual.size(), summary());
        }

        return this;
    }

    private boolean containsViolationWithPath(Set<? extends ConstraintViolation> violations, String path) {
        return violations.stream().anyMatch(violation -> violation.getPropertyPath().toString().equals(path));
    }
}