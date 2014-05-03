package halfpipe.router;

import com.netflix.zuul.context.ContextLifecycleFilter;
import halfpipe.util.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: spencergibb
 * Date: 4/24/14
 * Time: 8:57 PM
 */
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackageClasses = HalfpipeRouterApp.class)
public class HalfpipeRouterApp extends WebSecurityConfigurerAdapter {

    @Inject
    BeanUtils beanUtils;

    @Bean
    RouterProperties routerProperties() {
        return new RouterProperties();
    }

    @Bean
    public FilterRegistrationBean contextLifecycleFilter() {
        Collection<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new ContextLifecycleFilter());
        filterRegistrationBean.setUrlPatterns(urlPatterns);

        return filterRegistrationBean;
    }

    @Bean
    FilterIntializer filterIntializer() {
        return new FilterIntializer();
    }

    @Bean
    ZuulController zuulController() {
        return new ZuulController();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }

    public static void main(String[] args) {
        SpringApplication.run(HalfpipeRouterApp.class, args);
    }
}
