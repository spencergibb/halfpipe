package halfpipe.example.frontend;

import feign.Logger;
import halfpipe.client.BasicAuthenticationInterceptor;
import halfpipe.client.ClientConfigurer;
import halfpipe.example.client.PostClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = ExampleFrontendApp.class)
public class ExampleFrontendApp extends ClientConfigurer {

    @Bean
    public PostClient postClient() {
        return builder(PostClient.class)
                .requestInterceptor(new BasicAuthenticationInterceptor("test", "test123"))
                .logLevel(Logger.Level.FULL)
                .target("http://localhost:8080");
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleFrontendApp.class, args);
    }
}