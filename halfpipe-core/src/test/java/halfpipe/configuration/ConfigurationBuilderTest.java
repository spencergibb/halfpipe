package halfpipe.configuration;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import ch.qos.logback.classic.Level;
import com.google.common.collect.Lists;
import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.PropertyWrapper;
import halfpipe.configuration.builder.*;
import org.junit.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import halfpipe.configuration.convert.StringToTimeZoneConverter;
import halfpipe.context.MetricsContext;
import halfpipe.util.Duration;
import halfpipe.util.Size;

import java.util.List;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:21 PM
 */
public class ConfigurationBuilderTest {

    @Test
    public void testGet() throws Exception {
        ConcurrentMapConfiguration testProperties = new ConcurrentMapConfiguration();
        testProperties.addProperty("appName", "Test App");

        testProperties.addProperty("level", "INFO");
        testProperties.addProperty("duration", "1m");
        testProperties.addProperty("size", "1B");

        testProperties.addProperty("http.port", 80);
        testProperties.addProperty("http.gzip.enabled", true);

        testProperties.addProperty("longProp", 1);
        testProperties.addProperty("floatProp", 1.0);
        testProperties.addProperty("doubleProp", 1.0);


        DynamicPropertyFactory.initWithConfigurationSource(testProperties);

        TestConfiguration config = new TestConfiguration();
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToTimeZoneConverter());
        List<PropBuilder<?, ?>> builders = Lists.newArrayList();
        builders.add(new GenericBuilder());
        builders.add(new StringPropBuilder());
        builders.add(new IntBuilder());
        builders.add(new BooleanBuilder());
        builders.add(new LongBuilder());
        builders.add(new FloatBuilder());
        builders.add(new DoubleBuilder());
        new ConfigurationBuilder(conversionService, builders).build(config);

        assertProp("config", config);

        assertProp("config.appName", config.appName, "Test App");
        assertProp("config.defaultIntProp", config.defaultIntProp, 1);
        assertProp("config.defaultBooleanProp", config.defaultBooleanProp, true);
        assertProp("config.defaultLongProp", config.defaultLongProp, 1L);
        assertProp("config.defaultFloatProp", config.defaultFloatProp, 1.0f);
        assertProp("config.defaultDoubleProp", config.defaultDoubleProp, 1.0d);
        assertProp("config.defaultStringProp", config.defaultStringProp, "one");
        assertProp("config.longProp", config.longProp, 1L);
        assertProp("config.floatProp", config.floatProp, 1.0f);
        assertProp("config.doubleProp", config.doubleProp, 1.0d);

        assertProp("config.http", config.http);
        assertProp("config.http.port", config.http.port, 80);
        assertProp("config.http.viewPattern", config.http.viewPattern, "/*");
        assertProp("config.http.port", config.http.maxIdleTime, Duration.seconds(200));

        assertProp("config.http.gzip", config.http.gzip);
        assertProp("config.http.gzip.enabled", config.http.gzip.enabled, true);

        assertProp("config.level", config.level);
        assertProp("config.level", config.level, Level.INFO);

        assertProp("config.defaultLevel", config.defaultLevel);
        assertProp("config.defaultLevel", config.defaultLevel, Level.WARN);

        assertProp("config.size", config.size);
        assertProp("config.size", config.size, Size.bytes(1));

        assertProp("config.defaultSize", config.defaultSize);
        assertProp("config.defaultSize", config.defaultSize, Size.bytes(2));

        assertProp("config.duration", config.duration);
        assertProp("config.duration", config.duration, Duration.minutes(1));

        assertProp("config.defaultDuration", config.defaultDuration);
        assertProp("config.defaultDuration", config.defaultDuration, Duration.minutes(2));

        if (config.appConfigClass != MetricsContext.class) {
            throw new Exception();
        }
    }

    private <T> void assertProp(String propName, DynamicProp<T> property, T expected) {
        assertProp(propName, property);
        assertThat(propName +" is bad", property.getValue(), is(expected));
    }

    private <T> void assertProp(String propName, PropertyWrapper<T> property, T expected) {
        assertProp(propName, property);
        assertThat(propName +" is bad", property.getValue(), is(expected));
    }

    private void assertProp(String propName, Object property) {
        assertThat(propName +" is null", property, is(notNullValue()));
    }
}
