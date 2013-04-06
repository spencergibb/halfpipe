package halfpipe.groovyexample

import com.netflix.config.DynamicStringProperty
import halfpipe.configuration.Configuration
import org.springframework.stereotype.Component

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 9:01 PM
 */
@Component
class ExampleConf extends Configuration {

    public DynamicStringProperty helloText
}
