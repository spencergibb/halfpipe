package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import ch.qos.logback.classic.Level;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicStringProperty;

import java.util.TimeZone;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
public class LoggingConfiguration {

    public static class ConsoleConfiguration {
        public DynamicBooleanProperty enabled = prop(true);

        public DynamicProp<Level> threshold = prop(Level.ALL);

        public DynamicStringProperty logFormat;

        public DynamicProp<TimeZone> timeZone = prop(TimeZone.getTimeZone("UTC"));
    }

    public ConsoleConfiguration console;

    public DynamicProp<Level> level = prop(Level.WARN);
}
