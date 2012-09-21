package thirtytwo.degrees;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * User: gibbsb
 * Date: 9/21/12
 * Time: 4:18 PM
 *
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
