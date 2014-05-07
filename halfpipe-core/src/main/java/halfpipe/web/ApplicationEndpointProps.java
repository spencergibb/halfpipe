package halfpipe.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: spencergibb
 * Date: 5/7/14
 * Time: 10:10 AM
 */
@ConfigurationProperties("application.endpoint")
@Data
public class ApplicationEndpointProps extends EndpointProperties {
    protected String id = "application";
}
