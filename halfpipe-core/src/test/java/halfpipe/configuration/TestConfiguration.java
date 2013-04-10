package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import ch.qos.logback.classic.Level;
import com.netflix.config.*;
import halfpipe.context.MetricsContext;
import halfpipe.util.Duration;
import halfpipe.util.Size;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 12:09 AM
 */
public class TestConfiguration extends Configuration {
    DynaProp<Integer> defaultIntProp = prop(1);

    DynaProp<Boolean> defaultBooleanProp = prop(true);

    DynaProp<Long> defaultLongProp = prop(1L);

    DynaProp<Float> defaultFloatProp = prop(1.0f);

    DynaProp<Double> defaultDoubleProp = prop(1.0d);

    DynaProp<String> defaultStringProp = prop("one");

    DynaProp<Level> defaultLevel = prop(Level.WARN);

    DynaProp<Level> level;

    DynaProp<Size> defaultSize = prop(Size.bytes(2));

    DynaProp<Size> size;

    DynaProp<Duration> defaultDuration = prop(Duration.minutes(2));

    DynaProp<Duration> duration;

    DynaProp<Long> longProp;

    DynaProp<Float> floatProp;

    DynaProp<Double> doubleProp;

    Class<?> appConfigClass = MetricsContext.class;

}
