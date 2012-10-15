package thirtytwo.degrees.halfpipe.configuration;

import ch.qos.logback.classic.Level;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicStringProperty;

import javax.ws.rs.DefaultValue;
import java.util.TimeZone;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
public class LoggingConfiguration {

    public static class ConsoleConfiguration {
        @DefaultValue("true")
        public DynamicBooleanProperty enabled;

        @DefaultValue("ALL")
        public DynamicProp<Level> threshold;

        public DynamicStringProperty logFormat;

        @DefaultValue("UTC")
        public DynamicProp<TimeZone> timeZone;
    }

    public ConsoleConfiguration console;

    @DefaultValue("WARN")
    public DynamicProp<Level> level;
}
