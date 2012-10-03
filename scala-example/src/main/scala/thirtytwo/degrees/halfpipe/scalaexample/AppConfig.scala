package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation._
import com.netflix.config.scala.DynamicProperties
import javax.inject.Named
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller
import thirtytwo.degrees.halfpipe.DefaultScalaAppConfig
import thirtytwo.degrees.halfpipe.config.MetricsConfig

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[AppConfig]),
  excludeFilters = Array(new Filter (Array (classOf[Controller], classOf[Configuration]))))
@Import(Array(classOf[DefaultScalaAppConfig], classOf[SecurityConfig], classOf[MetricsConfig]))
class AppConfig extends DynamicProperties {

  @Bean @Named("helloText")
  def helloText() = dynamicStringProperty("hello.text", "Hello default")

}