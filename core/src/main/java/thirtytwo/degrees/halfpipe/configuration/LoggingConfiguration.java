package thirtytwo.degrees.halfpipe.configuration;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import ch.qos.logback.classic.Level;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicStringProperty;

import javax.ws.rs.DefaultValue;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
public class LoggingConfiguration {

    public static class ConsoleConf {
        @DefaultValue("true")
        public DynamicBooleanProperty enabled;

        @DefaultValue("ALL")
        public DynamicProp<Level> threshold;

        public DynamicStringProperty logFormat;
    }

    public ConsoleConf console;

    @DefaultValue("WARN")
    public DynamicProp<Level> level;
}
