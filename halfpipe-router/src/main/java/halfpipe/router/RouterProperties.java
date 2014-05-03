package halfpipe.router;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: spencergibb
 * Date: 5/2/14
 * Time: 9:20 PM
 */
@ConfigurationProperties("router")
@Data
public class RouterProperties {
    private String filterRoot;
}
