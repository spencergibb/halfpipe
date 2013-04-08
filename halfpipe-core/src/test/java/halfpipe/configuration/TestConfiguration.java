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
    DynamicIntProperty defaultIntProp = prop(1);

    DynamicBooleanProperty defaultBooleanProp = prop(true);

    DynamicLongProperty defaultLongProp = prop(1L);

    DynamicFloatProperty defaultFloatProp = prop(1.0f);

    DynamicDoubleProperty defaultDoubleProp = prop(1.0d);

    DynamicStringProperty defaultStringProp = prop("one");

    DynamicProp<Level> defaultLevel = prop(Level.WARN);

    DynamicProp<Level> level;

    DynamicProp<Size> defaultSize = prop(Size.bytes(2));

    DynamicProp<Size> size;

    DynamicProp<Duration> defaultDuration = prop(Duration.minutes(2));

    DynamicProp<Duration> duration;

    DynamicLongProperty longProp;

    DynamicFloatProperty floatProp;

    DynamicDoubleProperty doubleProp;

    Class<?> appConfigClass = MetricsContext.class;

}
