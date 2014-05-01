package halfpipe.logging;

import ch.qos.logback.classic.Level;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 11:32 AM
 * TODO: move to ApplicationContextInitializer to be earlier?
 */
@Configuration
@ConditionalOnClass(Level.class)
public class LoggingAutoConfig {

    @Bean
    LogInjector logInjector() {
        return new LogInjector();
    }

    @Bean
    LoggingProperties loggingProperties() {
        return new LoggingProperties();
    }

    @Bean
    LogbackConfigurer logbackConfigurer() {
        return new LogbackConfigurer();
    }

    @Bean(name = "logging.level.callback")
    LevelCallback levelCallback() {
        return new LevelCallback();
    }

    //TODO: create logging.loggers.callback
}
