package thirtytwo.degrees.halfpipe.config;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.web.servlet.config.annotation.*;

/**
 * User: spencergibb
 * Date: 9/25/12
 * Time: 3:39 AM
 */
public abstract class AbstractViewConfig extends WebMvcConfigurerAdapter {
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
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }


}
