package thirtytwo.degrees.halfpipe.configuration;

import com.netflix.config.*;
import thirtytwo.degrees.halfpipe.context.MetricsContext;

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

    DynamicLongProperty longProp;

    DynamicFloatProperty floatProp;

    DynamicDoubleProperty doubleProp;

    Class<?> appConfigClass = MetricsContext.class;

}
