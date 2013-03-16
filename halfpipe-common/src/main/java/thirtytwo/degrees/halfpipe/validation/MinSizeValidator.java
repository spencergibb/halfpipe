package thirtytwo.degrees.halfpipe.validation;

import thirtytwo.degrees.halfpipe.util.Size;
import thirtytwo.degrees.halfpipe.util.SizeUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that a {@link Size} being validated is greater than or equal to the
 * minimum value specified.
 * see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation
 */
public class MinSizeValidator implements ConstraintValidator<MinSize, Size> {

    private long minQty;
    private SizeUnit minUnit;

    @Override
    public void initialize(MinSize minValue) {
        this.minQty = minValue.value();
        this.minUnit = minValue.unit();
    }

    @Override
    public boolean isValid(Size value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || value.toBytes() >= minUnit.toBytes(minQty);
    }
}
