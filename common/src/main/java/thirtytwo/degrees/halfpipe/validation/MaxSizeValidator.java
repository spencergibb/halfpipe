package thirtytwo.degrees.halfpipe.validation;

import thirtytwo.degrees.halfpipe.util.Size;
import thirtytwo.degrees.halfpipe.util.SizeUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that a {@link Size} being validated is less than or equal to the
 * minimum value specified.
 * see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation
 */
public class MaxSizeValidator implements ConstraintValidator<MaxSize, Size> {

    private long maxQty;
    private SizeUnit maxUnit;

    @Override
    public void initialize(MaxSize minValue) {
        this.maxQty = minValue.value();
        this.maxUnit = minValue.unit();
    }

    @Override
    public boolean isValid(Size value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || value.toBytes() <= maxUnit.toBytes(maxQty);
    }
}
