package thirtytwo.degrees.halfpipe.validation.tests;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import thirtytwo.degrees.halfpipe.validation.ValidationMethod;
import thirtytwo.degrees.halfpipe.validation.Validator;

import javax.validation.Valid;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

// see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation/tests
@SuppressWarnings({"FieldMayBeFinal","MethodMayBeStatic","UnusedDeclaration"})
public class MethodValidatorTest {
    public static class SubExample {
        @ValidationMethod(message = "also needs something special")
        public boolean isOK() {
            return false;
        }
    }

    public static class Example {
        @Valid
        private SubExample subExample = new SubExample();

        @ValidationMethod(message = "must have a false thing")
        public boolean isFalse() {
            return false;
        }

        @ValidationMethod(message = "must have a true thing")
        public boolean isTrue() {
            return true;
        }
    }

    @Test
    public void complainsAboutMethodsWhichReturnFalse() throws Exception {
        assertThat(new Validator().validate(new Example()),
                   is(ImmutableList.of("must have a false thing", "subExample also needs something special")));
    }
}
