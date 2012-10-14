package thirtytwo.degrees.halfpipe.configuration.json;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.google.common.collect.Lists;
import com.netflix.config.*;
import com.netflix.config.sources.URLConfigurationSource;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;

/**
 * User: gibbsb
 * Date: 10/13/12
 * Time: 3:11 PM
 */
public class JSONParserTest {

    public static final String JSON = "{"+
            "     \"string\"  : \"foo\","+
            "     \"number\"  : 123456,"+
            "     \"boolean\" : true,"+
            "     \"null\"    : null,"+
            "     \"array\"   : [ 1, 2, 3 ],"+
            "     \"map\"     : { \"foo\" : \"bar\" },"+
            "     \"nested\"  : [ { \"foo0\" : \"bar0\" }, { \"foo1\" : \"bar1\" } ]"+
            "}";

    @Test
    public void testGet() throws Exception {
        JSONConfiguration config = new JSONConfiguration();
        config.load(new StringReader(JSON));

        String s = config.getString("string");
        assertThat("string json bad", s, is("foo"));

        int n = config.getInt("number");
        assertThat("number json bad", n, is(123456));

        boolean b = config.getBoolean("boolean");
        assertThat("boolean json bad", b, is(true));

        Object o = config.getString("null");
        assertThat("null json bad", o, is(nullValue()));

        List<BigInteger> array = config.getList(BigInteger.class, "array");
        assertThat("array json bad", array, is(hasItems(bi(1), bi(3), bi(3))));

        String mapVal = config.getString("map.foo");
        assertThat("mapVal json bad", mapVal, is("bar"));
    }

    BigInteger bi(int i) {
        return new BigInteger(String.valueOf(i));
    }

    @Test
    public void testArchaius() throws Exception {
        JSONConfiguration myConfiguration = new JSONConfiguration(); // this is your original configuration
        myConfiguration.load(new StringReader(JSON));

        AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(); // or use your own scheduler
        PolledConfigurationSource source = new URLConfigurationSource(); // or use your own source
        scheduler.setIgnoreDeletesFromSource(true); // don't treat properties absent from the source as deletes
        scheduler.startPolling(source, new ConcurrentMapConfiguration(myConfiguration));

        ConfigurationManager.install(myConfiguration);

        DynamicPropertyFactory props = DynamicPropertyFactory.getInstance();

        String s = props.getStringProperty("string", null).get();
        assertThat("string json bad", s, is("foo"));

        int n = props.getIntProperty("number", -1).get();
        assertThat("number json bad", n, is(123456));

        boolean b = props.getBooleanProperty("boolean", false).get();
        assertThat("boolean json bad", b, is(true));

        Object o = props.getStringProperty("null", null).get();
        assertThat("null json bad", null, is(nullValue()));

        String sub = props.getStringProperty("map.foo", null).get();
        assertThat("map.foo json bad", sub, is("bar"));
    }
}