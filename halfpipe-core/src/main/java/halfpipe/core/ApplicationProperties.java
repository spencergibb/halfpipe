package halfpipe.core;

import halfpipe.web.EmbeddedWar;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    private List<String> routes = new ArrayList<>();

    //TODO: list of embedded wars
    private EmbeddedWar embeddedWar = new EmbeddedWar();
}
