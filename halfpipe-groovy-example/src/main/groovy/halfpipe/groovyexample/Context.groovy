package halfpipe.groovyexample

import halfpipe.context.DefaultContext
import halfpipe.context.MetricsContext
import halfpipe.mgmt.resources.GCResource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource
import org.springframework.stereotype.Controller

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 8:57 PM
 */
@Configuration
@ComponentScan(basePackageClasses = [Context.class, GCResource.class], excludeFilters = [
    @ComponentScan.Filter(Controller.class),
    @ComponentScan.Filter(Configuration.class)
])
@ImportResource("classpath:META-INF/spring/applicationContext-security.xml")
@Import([DefaultContext.class, MetricsContext.class])
class Context {

    @Bean
    GCResource garbageCollectionTask() {
        return new GCResource()
    }
}
