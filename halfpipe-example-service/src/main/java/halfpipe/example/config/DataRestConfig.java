package halfpipe.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: spencergibb
 * Date: 4/10/14
 * Time: 9:25 PM
 */
@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class DataRestConfig extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
/*        try {
            config.setBaseUri(new URI("http://blah"));
        } catch (URISyntaxException e) {
            e.printStackTrace();  //TODO: implement catch
        }*/
    }
}
