package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation._
import com.netflix.config.scala.DynamicProperties
import javax.inject.{Inject, Named}
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller
import thirtytwo.degrees.halfpipe.DefaultScalaContext
import thirtytwo.degrees.halfpipe.context.MetricsContext
import thirtytwo.degrees.halfpipe.mgmt.resources.GCResource
//import thirtytwo.degrees.halfpipe.finagle.{FinagleClientCommand, FinagleCommand}
import org.springframework.context.ApplicationContext

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[Context], classOf[GCResource]),
  excludeFilters = Array(new Filter (Array (classOf[Controller], classOf[Configuration]))))
@Import(Array(classOf[DefaultScalaContext], classOf[SecurityContext], classOf[MetricsContext]))
class Context extends DynamicProperties {

  @Inject
  var config: ExampleScalaConfig = _

  @Inject
  var context: ApplicationContext = _

  @Bean @Named("helloText")
  def helloText() = dynamicStringProperty("hello.text", "Hello default")

  @Bean
  def garbageCollectionTask: GCResource = new GCResource

  /*@Bean
  def finagleCommand: FinagleCommand = new FinagleCommand(config, context)

  @Bean
  def finagleClient: FinagleClientCommand = new FinagleClientCommand(config)*/
}