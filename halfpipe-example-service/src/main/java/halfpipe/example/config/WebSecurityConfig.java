package halfpipe.example.config;

import halfpipe.example.properties.ExampleServiceProps;
import halfpipe.core.ApplicationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE - 6)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    ApplicationProperties applicationProperties;

    @Inject
    ExampleServiceProps props;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/v1/**").hasRole("USER")
                .anyRequest().authenticated();
        http.httpBasic().realmName(applicationProperties.getId())
            .and()
            .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.inMemoryAuthentication()
                .withUser(props.getUser().get())
                .password(props.getPassword().get())
                .roles("USER");
    }
}