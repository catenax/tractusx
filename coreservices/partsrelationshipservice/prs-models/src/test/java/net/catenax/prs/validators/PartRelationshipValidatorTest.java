package net.catenax.prs.validators;

import net.catenax.prs.testing.BaseDtoMother;
import net.catenax.prs.testing.DtoMother;
import org.junit.jupiter.params.ParameterizedTest;

public class PartRelationshipValidatorTest {

    /**
     * Base Object Mother to generate core DTO data for testing.
     */
    protected static final BaseDtoMother generateBaseDto = new BaseDtoMother();
    /**
     * Object Mother to generate core DTO data for testing.
     */
    protected static final DtoMother generateDto = new DtoMother();

    PartRelationshipValidator sut = new PartRelationshipValidator();

    @ParameterizedTest(name = "{index} {0}")
    public void validate() {

    }
}
