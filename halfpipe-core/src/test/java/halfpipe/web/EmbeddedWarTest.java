package halfpipe.web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Bean
    WebAppContext webAppContext () {
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("src/test/resources/helloworld.war");
        webapp.setExtractWAR(false);
        return webapp;
    }

    @Controller
    public static class WarController {

        private final WebAppContext webapp;

        @Inject
        public WarController(WebAppContext webapp) throws Exception {
            this.webapp = webapp;
            this.webapp.start();
        }

        //TODO: create handler mapping and war config object
        @RequestMapping("/hello/**")
        public void handleWar(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException, ServletException {
            Request jettyRequest = findRequest(servletRequest);
            String requestURI = servletRequest.getRequestURI();
            requestURI = requestURI.substring("/hello".length());
            webapp.handle(requestURI, jettyRequest, servletRequest, servletResponse);
        }

        protected Request findRequest(Object o) {
            if (o instanceof Request) {
                return (Request) o;
            }
            if (o instanceof HttpServletRequestWrapper) {
                HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper) o;
                return findRequest(wrapper.getRequest());
            }
            return null;
        }
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(final WebAppContext webapp) {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer containerFactory) {
                if (containerFactory instanceof JettyEmbeddedServletContainerFactory) {
                    JettyEmbeddedServletContainerFactory factory = (JettyEmbeddedServletContainerFactory) containerFactory;
                    factory.addServerCustomizers(new JettyServerCustomizer() {
                        @Override
                        public void customize(Server server) {
                            webapp.setServer(server);
                        }
                    });
                }
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(EmbeddedWarTest.class, args);
    }
}