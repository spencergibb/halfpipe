package halfpipe.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.ServletContext;

@Configuration
@EnableAutoConfiguration
public class EmbeddedWarTest extends WebSecurityConfigurerAdapter {


    @Bean
    ServletContext servletContext() {
        return new MockServletContext();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.inMemoryAuthentication()
                .withUser("test")
                .password("test123")
                .roles("USER");
    }

    public static void main(String[] args) {
        String[] config = new String[] {
            "--application.id=testEmbeddedWarApp",
            "--application.embeddedWar.path=/hello",
            "--application.embeddedWar.location=src/test/resources/helloworld.war",
        };
        SpringApplication.run(EmbeddedWarTest.class, config);
    }
}