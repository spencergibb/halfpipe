package thirtytwo.degrees.halfpipe.example.view;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import thirtytwo.degrees.halfpipe.context.AbstractViewContext;
import thirtytwo.degrees.halfpipe.context.MetricsContext;
import thirtytwo.degrees.halfpipe.mgmt.view.MgmtControllers;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = {ViewContext.class, MgmtControllers.class}, excludeFilters = {
        @Filter(Configuration.class)
})
@Import({MetricsContext.class})
public class ViewContext extends AbstractViewContext {
    //TODO: is this needed, can I set a welcome page?
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
