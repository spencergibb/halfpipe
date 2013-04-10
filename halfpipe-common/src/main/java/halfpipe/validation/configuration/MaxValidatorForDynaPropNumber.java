package halfpipe.validation.configuration;

import halfpipe.configuration.DynaProp;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: spencergibb
 * Date: 4/9/13
 * Time: 1:37 AM
 */
@Component
public class MaxValidatorForDynaPropNumber implements ConstraintValidator<Max, DynaProp<? extends Number>> {

    private long maxValue;

    @Override
    public void initialize(Max maxValue) {
        this.maxValue = maxValue.value();
    }

    @Override
    public boolean isValid(DynaProp<? extends Number> prop, ConstraintValidatorContext context) {
        Number value = prop.getValue();
        //null values are valid
        if ( value == null ) {
            return true;
        }
        if ( value instanceof BigDecimal) {
            return ( ( BigDecimal ) value ).compareTo( BigDecimal.valueOf( maxValue ) ) != 1;
        }
        else if ( value instanceof BigInteger) {
            return ( ( BigInteger ) value ).compareTo( BigInteger.valueOf( maxValue ) ) != 1;
        }
        else {
            long longValue = value.longValue();
            return longValue <= maxValue;
        }
    }
}
