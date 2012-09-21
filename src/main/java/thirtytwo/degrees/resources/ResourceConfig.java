package thirtytwo.degrees.resources;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * User: gibbsb
 * Date: 9/21/12
 * Time: 4:18 PM
 * <p/>
 * http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-enable
 */
@Configuration
@ComponentScan(basePackageClasses = ResourceConfig.class)
public class ResourceConfig {

}
