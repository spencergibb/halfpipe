package thirtytwo.degrees.halfpipe.configuration;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.netflix.config.PropertyWrapper;
import org.junit.Test;
import thirtytwo.degrees.halfpipe.context.MetricsContext;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:21 PM
 */
public class ConfigurationBuilderTest {

    @Test
    public void testGet() throws Exception {
        System.setProperty("archaius.configurationSource.defaultFileName", "configurationFactoryTest.properties");

        TestConfiguration config = new TestConfiguration();
        Configuration c = config;
        new ConfigurationBuilder().build(config);

        assertProp("config", config);

        assertProp("config.appName", config.appName, "Test App");
        assertProp("config.defaultIntProp", config.defaultIntProp, 1);
        assertProp("config.defaultBooleanProp", config.defaultBooleanProp, true);
        assertProp("config.defaultLongProp", config.defaultLongProp, 1L);
        assertProp("config.defaultFloatProp", config.defaultFloatProp, 1.0f);
        assertProp("config.defaultDoubleProp", config.defaultDoubleProp, 1.0d);
        assertProp("config.longProp", config.longProp, 1L);
        assertProp("config.floatProp", config.floatProp, 1.0f);
        assertProp("config.doubleProp", config.doubleProp, 1.0d);

        assertProp("config.http", config.http);
        assertProp("config.http.port", config.http.port, 80);
        assertProp("config.http.viewPattern", config.http.viewPattern, "/*");

        assertProp("config.http.gzip", config.http.gzip);
        assertProp("config.http.gzip.enabled", config.http.gzip.enabled, true);

        if (config.appConfigClass != MetricsContext.class) {
            throw new Exception();
        }
    }

    private <T> void assertProp(String propName, PropertyWrapper<T> property, T expected) {
        assertProp(propName, property);
        assertThat(propName +" is bad", property.getValue(), is(expected));
    }

    private void assertProp(String propName, Object property) {
        assertThat(propName +" is null", property, is(notNullValue()));
    }
}
