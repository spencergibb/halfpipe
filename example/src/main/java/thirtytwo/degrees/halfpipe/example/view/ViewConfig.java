package thirtytwo.degrees.halfpipe.example.view;

import org.springframework.context.annotation.*;
import thirtytwo.degrees.halfpipe.config.DefaultViewConfg;
import thirtytwo.degrees.halfpipe.web.admin.AdminControllers;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@Configuration
@ComponentScan(basePackageClasses = {ViewConfig.class})
@Import(DefaultViewConfg.class)
public class ViewConfig {
}
