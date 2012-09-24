package thirtytwo.degrees.halfpipe.scalaexample

import org.springframework.context.annotation._
import com.netflix.config.scala.DynamicProperties
import javax.inject.Named
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.stereotype.Controller
import com.codahale.jersey.inject.ScalaCollectionsQueryParamInjectableProvider
import com.codahale.jerkson.ScalaModule
import com.fasterxml.jackson.datatype.guava.GuavaModule
import thirtytwo.degrees.halfpipe.jersey.HalfpipeScalaObjectMapperProvider
import com.yammer.metrics.util.DeadlockHealthCheck
import scala.collection.JavaConversions._
import com.yammer.metrics.core.HealthCheck
import com.yammer.metrics.HealthChecks
import java.util.{List => JList}

@Configuration
@ComponentScan (basePackageClasses = Array (classOf[AppConfig]),
  excludeFilters = Array(new Filter (Array (classOf[Controller], classOf[Configuration]))))
@ImportResource(Array ("classpath:META-INF/spring/applicationContext-security.xml"))
class AppConfig extends DynamicProperties {

  @Bean @Named("helloText")
  def helloText() = dynamicStringProperty("hello.text", "Hello default")

  @Bean
  def deadlockHealthCheck() = new DeadlockHealthCheck()

  @Bean
  def healthChecks(healthChecks: JList[HealthCheck]) = {
    healthChecks foreach { healthCheck =>
      HealthChecks.register(healthCheck)
    }
    HealthChecks.defaultRegistry()
  }

  @Bean @Scope
  def jacksonScalaCollections() = new ScalaCollectionsQueryParamInjectableProvider()

  @Bean
  def scalaModule() = new ScalaModule(Thread.currentThread().getContextClassLoader)

  @Bean
  def guavaModule() = new GuavaModule

  @Bean @Scope
  def objectMapperProvider() = new HalfpipeScalaObjectMapperProvider(scalaModule(), guavaModule())

}