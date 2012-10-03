#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.view;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import thirtytwo.degrees.halfpipe.config.AbstractViewConfig;
import thirtytwo.degrees.halfpipe.config.DefaultViewConfg;
import thirtytwo.degrees.halfpipe.mgmt.view.MgmtControllers;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html${symbol_pound}mvc-config-enable
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = {ViewConfig.class, MgmtControllers.class}, excludeFilters = {
        @ComponentScan.Filter(Configuration.class)
})
public class ViewConfig extends AbstractViewConfig {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
