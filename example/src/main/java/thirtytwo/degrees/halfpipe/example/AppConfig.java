package thirtytwo.degrees.halfpipe.example;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.netflix.config.*;
import com.yammer.metrics.HealthChecks;
import com.yammer.metrics.core.HealthCheck;
import com.yammer.metrics.core.HealthCheckRegistry;
import com.yammer.metrics.util.DeadlockHealthCheck;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import thirtytwo.degrees.halfpipe.jersey.GuavaExtrasModule;
import thirtytwo.degrees.halfpipe.jersey.HalfpipeObjectMapperProvider;

import javax.inject.Named;
import java.util.List;

/**
 * User: spencer
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@Configuration
@ComponentScan(basePackageClasses = AppConfig.class, excludeFilters = {
    @Filter(Controller.class),
    @Filter(Configuration.class)
})
@ImportResource("classpath:META-INF/spring/applicationContext-security.xml")
public class AppConfig {

    //TODO: create a proxy that auto-populates a pojo full off values and watches for changes?
    // https://github.com/Netflix/archaius/wiki/Getting-Started
    @Bean @Named("helloText")
    public DynamicStringProperty helloText() {
        return DynamicPropertyFactory.getInstance().getStringProperty("hello.text", "Hello default text");
    }

    @Bean
    public DeadlockHealthCheck deadlockHealthCheck() {
        return new DeadlockHealthCheck();
    }

    @Bean
    public HealthCheckRegistry healthChecks(List<HealthCheck> healthChecks) {
        for (HealthCheck healthCheck : healthChecks) {
            HealthChecks.register(healthCheck);
        }
        return HealthChecks.defaultRegistry();
    }

    @Bean
    public GuavaExtrasModule guavaExtrasModule() {
        return new GuavaExtrasModule();
    }

    @Bean
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }

    @Bean @Scope
    public HalfpipeObjectMapperProvider objectMapperProvider() {
        return new HalfpipeObjectMapperProvider(guavaModule(), guavaExtrasModule());
    }

}
