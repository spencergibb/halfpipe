package thirtytwo.degrees.halfpipe.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.google.common.collect.Maps;
import com.yammer.metrics.logback.InstrumentedAppender;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import thirtytwo.degrees.halfpipe.configuration.LoggingConfiguration;

import java.util.Map;
import java.util.TimeZone;

/**
 * User: spencergibb
 * Date: 10/14/12
 * Time: 3:49 AM
 */
public class Logging {
    public static void bootstrap() {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.detachAndStopAllAppenders();

        LoggerContext context = root.getLoggerContext();
        root.addAppender(consoleLogger(context, Level.WARN, null, TimeZone.getDefault()));
    }

    private static ConsoleAppender<ILoggingEvent> consoleLogger(LoggerContext context, LoggingConfiguration.ConsoleConfiguration config)
    {
        return consoleLogger(context, config.threshold.get(), config.logFormat.get(),
                config.timeZone.get());
    }

    private static ConsoleAppender<ILoggingEvent> consoleLogger(LoggerContext context, Level level, String logFormat, TimeZone timeZone)
    {
        final LogFormatter formatter = new LogFormatter(context, timeZone);
        if (logFormat != null) {
            formatter.setPattern(logFormat);
        }
        formatter.start();

        final ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.setContext(context);
        appender.setLayout(formatter);
        final ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(level.toString());
        filter.start();
        appender.addFilter(filter);
        appender.start();

        return appender;
    }

    public static void configure(LoggingConfiguration config) {
        //hijackJDKLogging
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.getLoggerContext().reset();

        final LevelChangePropagator propagator = new LevelChangePropagator();
        propagator.setContext(root.getLoggerContext());
        propagator.setResetJUL(true);

        root.getLoggerContext().addListener(propagator);

        root.setLevel(config.level.get());

        Map<String, Level> loggers = Maps.newHashMap(); //TODO: config
        loggers.put("org.springframework.shell", Level.INFO);
        loggers.put("com.sun.jersey.api.core.ScanningResourceConfig", Level.INFO);
        loggers.put("thirtytwo.degrees.halfpipe", Level.INFO);
        for (Map.Entry<String, Level> entry : loggers.entrySet()) {
            ((Logger) LoggerFactory.getLogger(entry.getKey())).setLevel(entry.getValue());
        }

        if (config.console.enabled.get()) {
                root.addAppender(AsyncAppender.wrap(consoleLogger(root.getLoggerContext(),
                    config.console)));
        }

        final InstrumentedAppender appender = new InstrumentedAppender();
        appender.setContext(root.getLoggerContext());
        appender.start();
        root.addAppender(appender);
    }
}
