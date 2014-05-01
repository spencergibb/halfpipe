package halfpipe.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: spencergibb
 * Date: 4/16/14
 * Time: 11:53 AM
 */
@ConfigurationProperties("client")
@Data
public class ClientProperties {
    private String defaultGroup = "default";
}
