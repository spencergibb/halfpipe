package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import ch.qos.logback.classic.Level;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicStringMapProperty;
import com.netflix.config.DynamicStringProperty;
import halfpipe.validation.ValidationMethod;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.TimeZone;

/**
 * User: spencergibb
 * Date: 10/4/12
 * Time: 11:18 PM
 */
public class LoggingConfiguration {
    static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static class ConsoleConfiguration {
        public DynamicBooleanProperty enabled = prop(true);

        @NotNull
        public DynamicProp<Level> threshold = prop(Level.ALL);

        @NotNull
        public DynamicProp<TimeZone> timeZone = prop(UTC);

        public DynamicStringProperty logFormat;
    }

    public static class FileConfiguration {
        public DynamicBooleanProperty enabled = prop(false);

        @NotNull
        public DynamicProp<Level> threshold = prop(Level.ALL);

        public DynamicStringProperty currentLogFilename;

        public DynamicBooleanProperty archive = prop(true);

        public DynamicStringProperty archivedLogFilenamePattern;

        @Min(1)
        @Max(50)
        public DynamicIntProperty archivedFileCount = prop(5);

        @NotNull
        public DynamicProp<TimeZone> timeZone = prop(UTC);

        public DynamicStringProperty logFormat;

        @ValidationMethod(message = "must have logging.file.archivedLogFilenamePattern if logging.file.archive is true")
        public boolean isValidArchiveConfiguration() {
            return !enabled.get() || !archive.get() || (archivedLogFilenamePattern.get() != null);
        }

        @ValidationMethod(message = "must have logging.file.currentLogFilename if logging.file.enabled is true")
        public boolean isConfigured() {
            return !enabled.get() || (currentLogFilename.get() != null);
        }
    }

    public static class SyslogConfiguration {
        public enum Facility {
            AUTH, AUTHPRIV, DAEMON, CRON, FTP, LPR, KERN, MAIL, NEWS, SYSLOG, USER, UUCP,
            LOCAL0, LOCAL1, LOCAL2, LOCAL3, LOCAL4, LOCAL5, LOCAL6, LOCAL7;

            /*@Override
            public String toString() {
                return super.toString().replace("_", "+").toLowerCase(Locale.ENGLISH);
            }

            public static Facility parse(String facility) {
                return valueOf(facility.toUpperCase(Locale.ENGLISH).replace('+', '_'));
            }*/
        }

        public DynamicBooleanProperty enabled = prop(false);

        @NotNull
        public DynamicProp<Level> threshold = prop(Level.ALL);

        @NotNull
        public DynamicStringProperty host = prop("localhost");

        @NotNull
        public DynamicProp<Facility> facility = prop(Facility.LOCAL0);

        @NotNull
        public DynamicProp<TimeZone> timeZone = prop(UTC);

        public DynamicStringProperty logFormat;
    }

    @Valid
    @NotNull
    public ConsoleConfiguration console;

    @Valid
    @NotNull
    public FileConfiguration file;

    @Valid
    @NotNull
    public SyslogConfiguration syslog;

    public DynamicProp<Level> level = prop(Level.WARN);

    //TODO: either a DynamicMapProp from string or from yaml
    public DynamicStringMapProperty loggers;
}
