package halfpipe.example;

import static halfpipe.configuration.Defaults.*;

import com.netflix.config.DynamicStringProperty;
import halfpipe.configuration.Configuration;
import halfpipe.configuration.DynaProp;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 1:07 AM
 */
@Component
public class ExampleConfiguration extends Configuration {

    public DynamicStringProperty helloText;

    @Max(5)
    public DynaProp<Integer> helloInt = prop(4);
}
