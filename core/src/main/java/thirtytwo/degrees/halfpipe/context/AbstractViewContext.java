package thirtytwo.degrees.halfpipe.context;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static thirtytwo.degrees.halfpipe.Halfpipe.HALFPIPE_DEFAULT_SERVLET;
import static thirtytwo.degrees.halfpipe.Halfpipe.PROP_INSTALL_DEFAULT_SERVLET;

/**
 * User: spencergibb
 * Date: 9/25/12
 * Time: 3:39 AM
 */
public abstract class AbstractViewContext extends WebMvcConfigurerAdapter {
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
