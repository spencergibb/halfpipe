package thirtytwo.degrees.halfpipe.configuration;

import com.netflix.config.PropertyWrapper;
import org.junit.Test;
import thirtytwo.degrees.halfpipe.config.DefaultAppConfg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:21 PM
 */
public class ConfigurationFactoryTest {

    @Test
    public void testGet() throws Exception {
        System.setProperty("archaius.configurationSource.defaultFileName", "configurationFactoryTest.properties");

        TestConfiguration config = ConfigurationFactory.get(TestConfiguration.class);

        assertProp("config", config);

        assertProp("config.name", config.name, "Test App");
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
        assertProp("config.http.rootPath", config.http.rootPath, "/*");

        assertProp("config.http.gzip", config.http.gzip);
        assertProp("config.http.gzip.enabled", config.http.gzip.enabled, true);

        assertThat(config.appConfgClass, is(sameInstance(DefaultAppConfg.class)));
    }

    private <T> void assertProp(String propName, PropertyWrapper<T> property, T expected) {
        assertProp(propName, property);
        assertThat(propName +" is bad", property.getValue(), is(expected));
    }

    private void assertProp(String propName, Object property) {
        assertThat(propName +" is null", property, is(notNullValue()));
    }
}
