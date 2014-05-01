package halfpipe.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 3:12 AM
 */
@Component
@ConfigurationProperties("application")
@Data
public class ApplicationProperties {
    private String prefix;

    @NotNull
    private String id;
}
