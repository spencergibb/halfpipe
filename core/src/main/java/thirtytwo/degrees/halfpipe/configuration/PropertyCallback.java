package thirtytwo.degrees.halfpipe.configuration;

import java.lang.annotation.*;

/**
 * User: spencergibb
 * Date: 10/17/12
 * Time: 12:58 PM
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyCallback {
    Class value();
}
