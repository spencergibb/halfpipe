package halfpipe.consul;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 12:17 PM
 */
@ConfigurationProperties("halfpipe.consul")
@Data
public class ConsulProperties {
    private String host = "http://localhost:8500";
}
