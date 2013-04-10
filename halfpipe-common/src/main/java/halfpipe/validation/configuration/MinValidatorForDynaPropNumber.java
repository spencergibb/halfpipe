package halfpipe.validation.configuration;

import halfpipe.configuration.DynaProp;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: spencergibb
 * Date: 4/9/13
 * Time: 1:37 AM
 */
@Component
public class MinValidatorForDynaPropNumber implements ConstraintValidator<Min, DynaProp<? extends Number>> {

    private long minValue;

    public void initialize(Min minValue) {
        this.minValue = minValue.value();
    }

    public boolean isValid(DynaProp<? extends Number> prop, ConstraintValidatorContext constraintValidatorContext) {
        Number value = prop.getValue();
        //null values are valid
        if ( value == null ) {
            return true;
        }
        if ( value instanceof BigDecimal ) {
            return ( ( BigDecimal ) value ).compareTo( BigDecimal.valueOf( minValue ) ) != -1;
        }
        else if ( value instanceof BigInteger ) {
            return ( ( BigInteger ) value ).compareTo( BigInteger.valueOf( minValue ) ) != -1;
        }
        else {
            long longValue = value.longValue();
            return longValue >= minValue;
        }
    }
}
