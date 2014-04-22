package halfpipe.example;

import halfpipe.example.properties.ExampleServiceProps;
import halfpipe.properties.AbstractCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = ExampleServiceApp.class)
public class ExampleServiceApp {

    @Bean(name = "hello.callback")
    Runnable helloPropertiesCallback() {
        return new AbstractCallback<ExampleServiceProps, Object>() {
            @Override
            public void run() {
                logger.info("property named: {} changed to {}", prop.getName(), prop.getValue());
            }
        };
    }

    @Bean(name = "hello.defaultMessage.callback")
    Runnable helloDefaultMessageCallback() {
        return new AbstractCallback<ExampleServiceProps, String>() {
            @Override
            public void run() {
                logger.info("hello.defaultMessage changed to {}", prop.getValue());
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleServiceApp.class, args);
    }
}