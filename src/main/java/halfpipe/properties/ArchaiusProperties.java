package halfpipe.properties;

import java.lang.annotation.*;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 2:19 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArchaiusProperties {
    String value() default "";
}
