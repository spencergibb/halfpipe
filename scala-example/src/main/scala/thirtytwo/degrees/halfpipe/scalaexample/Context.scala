package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation._
import com.netflix.config.scala.DynamicProperties
import javax.inject.Named
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller
import thirtytwo.degrees.halfpipe.DefaultScalaContext
import thirtytwo.degrees.halfpipe.context.MetricsContext

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[Context]),
  excludeFilters = Array(new Filter (Array (classOf[Controller], classOf[Configuration]))))
@Import(Array(classOf[DefaultScalaContext], classOf[SecurityContext], classOf[MetricsContext]))
class Context extends DynamicProperties {

  @Bean @Named("helloText")
  def helloText() = dynamicStringProperty("hello.text", "Hello default")

}