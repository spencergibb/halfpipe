package halfpipe.example;

import com.netflix.config.DynamicStringProperty;
import org.springframework.stereotype.Component;
import halfpipe.configuration.Configuration;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 1:07 AM
 */
@Component
public class ExampleConfiguration extends Configuration {

    public DynamicStringProperty helloText;
}
