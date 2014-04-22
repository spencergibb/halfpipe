package halfpipe.example.frontend;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import halfpipe.consul.ConsulConfigurer;
import halfpipe.example.client.PostClient;
import halfpipe.example.model.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = ExampleFrontendApp.class)
public class ExampleFrontendApp extends ConsulConfigurer {

    @Bean
    public PostClient postClient() {
        return client()
                .requestInterceptor(new BasicAuthRequestInterceptor("test", "test123"))
                .logLevel(Logger.Level.FULL)
                .target(loadBalance(PostClient.class, "http://exampleService"));
    }

    @Bean(name = "postsAsync.fallback")
    Supplier<List<Post>> postsAsyncFallback() {
        return () -> Lists.newArrayList(getPost());
    }

    private Post getPost() {
        Post post = new Post();
        post.setId(-1L);
        post.setTitle("This is a fallback post for ");
        post.setAuthor("System");
        post.setBody("Hystrix caught an error, this is the fallback");
        return post;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleFrontendApp.class, args);
    }
}