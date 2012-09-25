package thirtytwo.degrees.halfpipe.config;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import thirtytwo.degrees.halfpipe.web.admin.AdminControllers;

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:34 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = AdminControllers.class)
public class DefaultViewConfg extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        if (DynamicPropertyFactory.getInstance().getBooleanProperty(PROP_INSTALL_DEFAULT_SERVLET, false).get()) {
            configurer.enable(HALFPIPE_DEFAULT_SERVLET);
        } else {
            configurer.enable();
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
