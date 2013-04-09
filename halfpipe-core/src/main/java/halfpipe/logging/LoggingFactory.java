package halfpipe.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.yammer.metrics.logback.InstrumentedAppender;
import halfpipe.configuration.Configuration;
import halfpipe.configuration.LoggingConfiguration;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
@Service
@Lazy(false)
public class LoggingFactory {
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

    Configuration config;
    LoggingConfiguration logging;

    @Inject
    public LoggingFactory(Configuration config) {
        this.config = config;
        this.logging = config.logging;
    }

    @PostConstruct
    public void configure() {
        hijackJDKLogging();

        final Logger root = configureLevels();

        /*TODO: move to configurable loggers
        for (LoggingOutput output : config.logging.outputs) {
            root.addAppender(output.build(root.getLoggerContext(), config.appName.get(), null));
        }*/

        if (logging.console.enabled.get()) {
            root.addAppender(AsyncAppender.wrap(LogbackFactory.buildConsoleAppender(logging.console,
                    root.getLoggerContext(),
                    Optional.fromNullable(logging.console.logFormat.get()))));
        }

        if (logging.file.enabled.get()) {
            root.addAppender(AsyncAppender.wrap(LogbackFactory.buildFileAppender(logging.file,
                    root.getLoggerContext(),
                    Optional.fromNullable(logging.file.logFormat.get()))));
        }

        if (logging.syslog.enabled.get()) {
            root.addAppender(AsyncAppender.wrap(LogbackFactory.buildSyslogAppender(logging.syslog,
                    root.getLoggerContext(),
                    config.appName.get(),
                    Optional.fromNullable(logging.syslog.logFormat.get()))));
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

    private void configureInstrumentation(Logger root) {
        final InstrumentedAppender appender = new InstrumentedAppender();
        appender.setContext(root.getLoggerContext());
        appender.start();
        root.addAppender(appender);
    }

    private void hijackJDKLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private Logger configureLevels() {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.getLoggerContext().reset();

        final LevelChangePropagator propagator = new LevelChangePropagator();
        propagator.setContext(root.getLoggerContext());
        propagator.setResetJUL(true);

        root.getLoggerContext().addListener(propagator);

        root.setLevel(logging.level.get());

        Map<String, Level> loggers = Maps.newHashMap();
        loggers.put("org.springframework.shell", Level.INFO);
        loggers.put("com.sun.jersey.api.core.ScanningResourceConfig", Level.INFO);
        loggers.put("halfpipe", Level.INFO);

        //DEFAULTS
        for (Map.Entry<String, Level> entry : loggers.entrySet()) {
            setLevel(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : logging.loggers.getMap().entrySet()) {
            setLevel(entry.getKey(), Level.toLevel(entry.getValue()));
        }

        return root;
    }

    private void setLevel(String logger, Level level) {
        ((Logger) LoggerFactory.getLogger(logger)).setLevel(level);
    }

    private static Logger getCleanRoot() {
        final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.detachAndStopAllAppenders();
        return root;
    }

}
