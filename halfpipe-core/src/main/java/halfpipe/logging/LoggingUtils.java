package halfpipe.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.google.common.collect.Maps;
import com.yammer.metrics.logback.InstrumentedAppender;
import halfpipe.configuration.Configuration;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import halfpipe.configuration.LoggingConfiguration;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.TimeZone;

/**
 * User: spencergibb
 * Date: 10/14/12
 * Time: 3:49 AM
 */
public class LoggingUtils {
    public static void bootstrap() {
        final Logger root = getCleanRoot();

        final LogFormatter formatter = new LogFormatter(root.getLoggerContext(),
                TimeZone.getDefault());
        formatter.start();

        final ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(Level.WARN.toString());
        filter.start();

        final ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.addFilter(filter);
        appender.setContext(root.getLoggerContext());
        appender.setLayout(formatter);
        appender.start();

        root.addAppender(appender);
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
        hijackJDKLogging();

        final Logger root = configureLevels(config);

        /*TODO: move to configurable loggers
        for (LoggingOutput output : config.logging.outputs) {
            root.addAppender(output.build(root.getLoggerContext(), config.appName.get(), null));
        }*/
        if (config.console.enabled.get()) {
            root.addAppender(AsyncAppender.wrap(consoleLogger(root.getLoggerContext(),
                    config.console)));
        }

        final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            final ObjectName objectName = new ObjectName("halfpipe:type=Logging");
            if (!server.isRegistered(objectName)) {
                server.registerMBean(new JMXConfigurator(root.getLoggerContext(),
                        server,
                        objectName),
                        objectName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        configureInstrumentation(root);
    }

    private static void configureInstrumentation(Logger root) {
        final InstrumentedAppender appender = new InstrumentedAppender();
        appender.setContext(root.getLoggerContext());
        appender.start();
        root.addAppender(appender);
    }

    private static void hijackJDKLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static Logger configureLevels(LoggingConfiguration logging) {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.getLoggerContext().reset();

        final LevelChangePropagator propagator = new LevelChangePropagator();
        propagator.setContext(root.getLoggerContext());
        propagator.setResetJUL(true);

        root.getLoggerContext().addListener(propagator);

        root.setLevel(logging.level.get());

        /*for (Map.Entry<String, Level> entry : logging.loggers.entrySet()) {
            ((Logger) LoggerFactory.getLogger(entry.getKey())).setLevel(entry.getValue());
        }*/
        Map<String, Level> loggers = Maps.newHashMap(); //TODO: config
        loggers.put("org.springframework.shell", Level.INFO);
        loggers.put("com.sun.jersey.api.core.ScanningResourceConfig", Level.INFO);
        loggers.put("halfpipe", Level.INFO);
        for (Map.Entry<String, Level> entry : loggers.entrySet()) {
            ((Logger) LoggerFactory.getLogger(entry.getKey())).setLevel(entry.getValue());
        }

        return root;
    }

    private static Logger getCleanRoot() {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.detachAndStopAllAppenders();
        return root;
    }

}
