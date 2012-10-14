package thirtytwo.degrees.halfpipe.configuration;

import ch.qos.logback.classic.Level;
import com.netflix.config.*;
import thirtytwo.degrees.halfpipe.context.MetricsContext;
import thirtytwo.degrees.halfpipe.util.Duration;
import thirtytwo.degrees.halfpipe.util.Size;

import javax.ws.rs.DefaultValue;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 12:09 AM
 */
public class TestConfiguration extends Configuration {
    @DefaultValue("1")
    DynamicIntProperty defaultIntProp;

    @DefaultValue("true")
    DynamicBooleanProperty defaultBooleanProp;

    @DefaultValue("1")
    DynamicLongProperty defaultLongProp;

    @DefaultValue("1.0f")
    DynamicFloatProperty defaultFloatProp;

    @DefaultValue("1.0d")
    DynamicDoubleProperty defaultDoubleProp;

    @DefaultValue("WARN")
    DynamicProp<Level> defaultLevel;

    DynamicProp<Level> level;

    @DefaultValue("2B")
    DynamicProp<Size> defaultSize;

    DynamicProp<Size> size;

    @DefaultValue("2m")
    DynamicProp<Duration> defaultDuration;

    DynamicProp<Duration> duration;

    DynamicLongProperty longProp;

    DynamicFloatProperty floatProp;

    DynamicDoubleProperty doubleProp;

    Class<?> appConfigClass = MetricsContext.class;

}
