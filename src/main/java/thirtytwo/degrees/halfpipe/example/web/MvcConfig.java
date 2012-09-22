package thirtytwo.degrees.halfpipe.example.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import thirtytwo.degrees.halfpipe.web.admin.AdminControllers;

/**
 * User: spencer
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = {MvcConfig.class, AdminControllers.class})
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
