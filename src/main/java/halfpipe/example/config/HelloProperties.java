package halfpipe.example.config;

import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import lombok.Data;
import org.springframework.stereotype.Component;

import static halfpipe.properties.DynamicProp.*;

/**
 * User: spencergibb
 * Date: 12/12/13
 * Time: 4:43 PM
 */
@Component
//@ConfigurationProperties("hello")
@ArchaiusProperties("hello")
@Data
public class HelloProperties {
    private DynaProp<String> defaultMessage = prop("HelloWorld");
}
