package halfpipe.router;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static java.util.concurrent.TimeUnit.*;

/**
 * User: spencergibb
 * Date: 5/2/14
 * Time: 9:20 PM
 */
@ConfigurationProperties("router")
@Data
public class RouterProperties {
    private String filterRoot;
    private long cacheRefresh = MINUTES.toMillis(5L);
}
