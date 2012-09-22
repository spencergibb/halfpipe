package thirtytwo.degrees;

import com.netflix.config.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;

import javax.inject.Named;

/**
 * User: gibbsb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@Configuration
@ComponentScan(basePackageClasses = AppConfig.class, excludeFilters = {
    @Filter(Controller.class)
})
public class AppConfig {

    //TODO: create a proxy that auto-populates a pojo full off values and watches for changes?
    // https://github.com/Netflix/archaius/wiki/Getting-Started
    @Bean @Named("helloText")
    public DynamicStringProperty helloText() {
        return DynamicPropertyFactory.getInstance().getStringProperty("hello.text", "Hello default text");
    }
}
