package halfpipe.example.client;

/**
 * User: spencergibb
 * Date: 5/27/14
 * Time: 9:16 AM
 */

import com.netflix.hystrix.HystrixExecutable;
import halfpipe.client.ClientConfigurer;
import halfpipe.client.TestClientBuilder;
import halfpipe.example.ExampleServiceApp;
import halfpipe.example.model.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import rx.Observable;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {PostClientIT.App.class, ExampleServiceApp.class})
@WebAppConfiguration
@IntegrationTest({
        "server.port=0",
        "application.id=testPostClient",
        "shell.ssh.enabled=false",
        "management.port=0",
})
public class PostClientIT {

    @EnableAutoConfiguration
    @Configuration
    @Import(ClientConfigurer.class)
    public static class App extends WebSecurityConfigurerAdapter {
        @Bean
        ServletContext servletContext() {
            return new MockServletContext();
        }

        @Bean
        TestClientBuilder clientBuilder() {
            return new TestClientBuilder();
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
            SpringApplication.run(PostClientIT.class);
        }
    }

    @Autowired
    TestClientBuilder clientBuilder;

    @Value("${local.server.port}")
    int port;

    PostClient client;

    @PostConstruct
    public void init() {
        String url = "http://localhost:" + port;
        client = clientBuilder.client()
                .target(PostClient.class, url);
    }

    @Test
    public void testPosts() {
        List<Post> posts = client.posts();
        assertNotNull("posts is null", posts);
    }

    @Test
    public void testPostsExecutable() {
        HystrixExecutable<List<Post>> executable = client.postsExecutable();
        List<Post> posts = executable.execute();
        assertNotNull("posts is null", posts);
    }

    @Test
    public void testPostsAsync() throws ExecutionException, InterruptedException {
        Future<List<Post>> future = client.postsAsync();
        List<Post> posts = future.get();
        assertNotNull("posts is null", posts);
    }

    @Test
    public void testPostsObserve() {
        Observable<List<Post>> observable = client.postsObserve();
        List<Post> posts = observable.toBlockingObservable().single();
        assertNotNull("posts is null", posts);
    }

}
