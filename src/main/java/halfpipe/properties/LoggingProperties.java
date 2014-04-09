package halfpipe.properties;

import ch.qos.logback.classic.Level;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 3:12 AM
 */
@Component
@ConfigurationProperties("logging")
@Data
public class LoggingProperties {
    private List<String> loggers;
    private Level level;
}
