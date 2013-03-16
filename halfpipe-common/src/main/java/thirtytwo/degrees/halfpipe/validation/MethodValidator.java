package thirtytwo.degrees.halfpipe.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A validator for {@link ValidationMethod}-annotated methods.
 * see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation
 */
public class MethodValidator implements ConstraintValidator<ValidationMethod, Boolean> {
    @Override
    public void initialize(ValidationMethod constraintAnnotation) {

    }

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return (value == null) || value;
    }
}
