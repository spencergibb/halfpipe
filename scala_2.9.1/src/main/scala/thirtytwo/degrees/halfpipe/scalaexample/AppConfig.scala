package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation.{ImportResource, Bean, Configuration, ComponentScan}
import com.netflix.config.DynamicPropertyFactory
import javax.inject.Named
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[AppConfig]),
  excludeFilters = Array(new Filter (Array (classOf[Controller]))))
@ImportResource(Array ("classpath:META-INF/spring/applicationContext-security.xml"))
class AppConfig {

  @Bean @Named("helloText")
  def helloText() = DynamicPropertyFactory.getInstance().getStringProperty("hello.text", "Hello default")

}