package halfpipe.example;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import halfpipe.context.DefaultContext;
import halfpipe.context.MetricsContext;
import halfpipe.mgmt.resources.GCResource;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@Configuration
@ComponentScan(basePackageClasses = {Context.class, GCResource.class}, excludeFilters = {
    @Filter(Controller.class),
    @Filter(Configuration.class)
})
//@ImportResource("classpath:META-INF/spring/applicationContext-security.xml")
@Import({DefaultContext.class, MetricsContext.class, SecurityContext.class})
public class Context {

    @Bean
    public GCResource garbageCollectionTask() {
        return new GCResource();
    }

}
