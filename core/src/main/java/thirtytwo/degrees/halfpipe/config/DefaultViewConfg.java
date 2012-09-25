package thirtytwo.degrees.halfpipe.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import thirtytwo.degrees.halfpipe.mgmt.view.MgmtControllers;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = MgmtControllers.class)
public class DefaultViewConfg extends AbstractViewConfig {

}
