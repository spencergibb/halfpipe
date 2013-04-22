package halfpipe.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.AuthenticationBuilder;
import org.springframework.security.config.annotation.web.EnableWebSecurity;
import org.springframework.security.config.annotation.web.ExpressionUrlAuthorizations;
import org.springframework.security.config.annotation.web.HttpConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 10:18 PM
 */
@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Override
    protected void registerAuthentication(AuthenticationBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
            .withUser("admin").password("password").roles("USER", "ADMIN").and()
            .withUser("user").password("password").roles("USER");
    }

    @Override
    protected void authorizeUrls(ExpressionUrlAuthorizations interceptUrls) {
        interceptUrls
                .antMatchers("/**/mgmt/**").hasRole("ADMIN")
                .antMatchers("/ws/**").hasRole("USER")
                ;
    }

    @Override
    protected void configure(HttpConfiguration http) throws Exception {
        http
            .httpBasic()
            ;
        //TODO: why doesn't above do this?
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("test");
        http.authenticationEntryPoint(entryPoint);
    }
}
