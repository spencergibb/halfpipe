package thirtytwo.degrees.halfpipe.example;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import thirtytwo.degrees.halfpipe.config.DefaultAppConfg;
import thirtytwo.degrees.halfpipe.config.MetricsConfig;
import thirtytwo.degrees.halfpipe.mgmt.resources.GCResource;

import javax.inject.Named;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@Configuration
@ComponentScan(basePackageClasses = {AppConfig.class, GCResource.class}, excludeFilters = {
    @Filter(Controller.class),
    @Filter(Configuration.class)
})
@ImportResource("classpath:META-INF/spring/applicationContext-security.xml")
@Import({DefaultAppConfg.class, MetricsConfig.class})
public class AppConfig {

    //TODO: create a proxy that auto-populates a pojo full off values and watches for changes?
    // https://github.com/Netflix/archaius/wiki/Getting-Started
    @Bean @Named("helloText")
    public DynamicStringProperty helloText() {
        return DynamicPropertyFactory.getInstance().getStringProperty("hello.text", "Hello default text");
    }

    @Bean
    public GCResource garbageCollectionTask() {
        return new GCResource();
    }

}
