package thirtytwo.degrees.halfpipe.example.web;

import com.yammer.metrics.reporting.MetricsServlet;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.ServletWrappingController;

/**
 * User: gibbsb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = MvcConfig.class)
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

    @Bean
    public ServletWrappingController metrics() {
        ServletWrappingController controller = new ServletWrappingController();
        controller.setServletClass(MetricsServlet.class);
        controller.setServletName("metrics");

        return controller;
    }
}
