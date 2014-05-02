package halfpipe.consul;

import halfpipe.web.EndpointProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: spencergibb
 * Date: 5/2/14
 * Time: 8:56 AM
 */
@ConfigurationProperties("consul.endpoint")
@Data
public class ConsulEndpointProps extends EndpointProperties {
    protected String id = "consul";
}
