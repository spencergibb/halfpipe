package halfpipe.validation.tests;

import com.google.common.collect.ImmutableList;
import halfpipe.validation.HalfpipeValidator;
import org.junit.Test;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

// see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation/tests
public class ValidatorTest {
    @SuppressWarnings("unused")
    public static class Example {
        @NotNull
        private String notNull = null;

        @Max(30)
        private int tooBig = 50;

        public void setNotNull(String notNull) {
            this.notNull = notNull;
        }

        public void setTooBig(int tooBig) {
            this.tooBig = tooBig;
        }
    }

    private final HalfpipeValidator validator = new HalfpipeValidator();

    @Test
    public void returnsASetOfErrorsForAnObject() throws Exception {
        if ("en".equals(Locale.getDefault().getLanguage())) {
            assertThat(validator.validate(new Example()),
                       is(ImmutableList.of("notNull may not be null (was null)",
                                           "tooBig must be less than or equal to 30 (was 50)")));
        }
    }

    @Test
    public void returnsAnEmptySetForAValidObject() throws Exception {
        final Example example = new Example();
        example.setNotNull("woo");
        example.setTooBig(20);

        assertThat(validator.validate(example),
                   is(ImmutableList.<String>of()));
    }
}
