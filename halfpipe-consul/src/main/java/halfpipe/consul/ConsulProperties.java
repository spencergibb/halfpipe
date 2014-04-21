package halfpipe.consul;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: spencergibb
 * Date: 4/18/14
 * Time: 12:17 PM
 */
@ConfigurationProperties("halfpipe.consul")
@Data
public class ConsulProperties {
    @NotNull
    private String host = "http://localhost:8500";

    @NotNull
    private String serviceName;

    private List<String> tags;

    private boolean enabled = true;
}
