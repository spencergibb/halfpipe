package halfpipe.example;

import halfpipe.example.properties.HelloProperties;
import halfpipe.properties.AbstractCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = ExampleApp.class)
public class ExampleApp {

    @Bean(name = "hello.callback")
    Runnable helloPropertiesCallback() {
        return new AbstractCallback<HelloProperties, Object>() {
            @Override
            public void run() {
                logger.info("property named: {} changed to {}", prop.getName(), prop.getValue());
            }
        };
    }

    @Bean(name = "hello.defaultMessage.callback")
    Runnable helloDefaultMessageCallback() {
        return new AbstractCallback<HelloProperties, String>() {
            @Override
            public void run() {
                logger.info("hello.defaultMessage changed to {}", prop.getValue());
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleApp.class, args);
    }
}