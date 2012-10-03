#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import thirtytwo.degrees.halfpipe.config.DefaultAppConfg;
import thirtytwo.degrees.halfpipe.mgmt.resources.GCResource;

import javax.inject.Named;

@Configuration
@ComponentScan(basePackageClasses = {AppConfig.class, GCResource.class}, excludeFilters = {
    @Filter(Controller.class),
    @Filter(Configuration.class)
})
@ImportResource("classpath:META-INF/spring/applicationContext-security.xml")
@Import(DefaultAppConfg.class)
public class AppConfig {

    @Bean @Named("helloText")
    public DynamicStringProperty helloText() {
        return DynamicPropertyFactory.getInstance().getStringProperty("hello.text", "Hello default text");
    }

    @Bean
    public GCResource garbageCollectionTask() {
        return new GCResource();
    }

}
