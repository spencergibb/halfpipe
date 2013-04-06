package halfpipe.groovyexample.view

import halfpipe.context.AbstractViewContext
import halfpipe.context.MetricsContext
import halfpipe.mgmt.view.MgmtControllers
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 11:49 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = [ViewContext.class, MgmtControllers.class], excludeFilters = [
    @ComponentScan.Filter(Configuration.class)
])
@Import(MetricsContext.class)
class ViewContext extends AbstractViewContext {
    @Override
    void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/static/index.html")
    }
}
