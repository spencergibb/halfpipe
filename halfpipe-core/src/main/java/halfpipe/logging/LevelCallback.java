package halfpipe.logging;

import ch.qos.logback.classic.Level;
import halfpipe.properties.AbstractCallback;
import halfpipe.properties.LoggingProperties;

import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/22/14
 * Time: 12:25 AM
 */
public class LevelCallback extends AbstractCallback<LoggingProperties, Level> {
    @Inject
    LogbackConfigurer configurer;


    @Override
    public void run() {
        logger.info("Setting rootLogger to updated level {}", prop.get());
        configurer.setLogLevel(null, prop.get());
    }
}
