package thirtytwo.degrees.halfpipe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import thirtytwo.degrees.halfpipe.web.admin.AdminControllers;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = AdminControllers.class)
public class DefaultViewConfg extends AbstractViewConfig {

}
