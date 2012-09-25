package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation._
import com.netflix.config.scala.DynamicProperties
import javax.inject.Named
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller
import thirtytwo.degrees.halfpipe.{SecurityConfig, DefaultScalaAppConfig}

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[AppConfig]),
  excludeFilters = Array(new Filter (Array (classOf[Controller], classOf[Configuration]))))
//@ImportResource(Array ("classpath:META-INF/spring/applicationContext-security.xml"))
@Import(Array(classOf[DefaultScalaAppConfig], classOf[SecurityConfig]))
class AppConfig extends DynamicProperties {

  @Bean @Named("helloText")
  def helloText() = dynamicStringProperty("hello.text", "Hello default")

}