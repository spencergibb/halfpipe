package halfpipe.web;

import halfpipe.core.ApplicationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EmbeddedWarTest.App.class)
@WebAppConfiguration
@IntegrationTest({
        "server.port=0",
        "application.id=testEmbeddedWarApp",
        "application.embeddedWar.enabled=true",
        "application.embeddedWar.path=/hello",
        "application.embeddedWar.location=src/test/resources/helloworld.war",
})
public class EmbeddedWarTest {

    @EnableAutoConfiguration
    @Configuration
    public static class App extends WebSecurityConfigurerAdapter  {
        @Bean
        ServletContext servletContext() {
            return new MockServletContext();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/**")
                    .permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
            authManagerBuilder.inMemoryAuthentication()
                    .withUser("test")
                    .password("test123")
                    .roles("USER");
        }

        public static void main(String[] args) {
            SpringApplication.run(EmbeddedWarTest.class);
        }
    }

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ApplicationProperties props;

    RestTemplate rest = new TestRestTemplate();

    @Test
    public void testRoot() {
        ResponseEntity<String> response = rest.getForEntity(getUrl("/"), String.class);
        assertResponse(response, "jsp");
    }

    @Test
    public void testHtml() {
        ResponseEntity<String> response = rest.getForEntity(getUrl("/hello.html"), String.class);
        assertResponse(response, "html");
    }

    @Test
    public void testSub() {
        ResponseEntity<String> response = rest.getForEntity(getUrl("/sub/sub.html"), String.class);
        assertResponse(response, "sub");
    }

    private void assertResponse(ResponseEntity<String> response, String value) {
        assertTrue("request not successful", response.getStatusCode().is2xxSuccessful());
        assertTrue("response doesn't contain Hello World", response.getBody().contains("Hello World"));
        assertTrue("response doesn't contain "+value, response.getBody().contains(value));
    }

    private String getUrl(String path) {
        return "http://localhost:" + port + props.getEmbeddedWar().getPath()+path;
    }
}