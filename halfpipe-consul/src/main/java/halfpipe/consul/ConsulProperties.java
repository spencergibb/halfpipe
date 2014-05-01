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
@ConfigurationProperties("consul")
@Data
public class ConsulProperties {
    @NotNull
    private String url = "http://localhost:8500";

    private List<String> tags;

    private boolean enabled = true;

    private List<String> clients;
}
