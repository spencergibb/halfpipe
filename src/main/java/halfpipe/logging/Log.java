package halfpipe.logging;

import java.lang.annotation.*;

/**
 * Marker for {@link org.slf4j.Logger} injection.
 *
 * @see LogInjector
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Log {

    Class<?> value() default DEFAULT.class;

    static class DEFAULT {}
}