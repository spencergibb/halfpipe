package thirtytwo.degrees.halfpipe

import config.DefaultAppConfg
import jersey.{GuavaExtrasModule, AnnotationSensitivePropertyNamingStrategy, HalfpipeScalaObjectMapperProvider, HalfpipeObjectMapperProvider}
import org.springframework.context.annotation.{Scope, Bean, Configuration}
import org.codehaus.jackson.map.ObjectMapper
import com.codahale.jerkson.ScalaModule
import com.yammer.metrics.util.DeadlockHealthCheck
import java.util.{List => JList}
import com.yammer.metrics.core.HealthCheck
import com.yammer.metrics.HealthChecks
import com.codahale.jersey.inject.ScalaCollectionsQueryParamInjectableProvider
import com.fasterxml.jackson.datatype.guava.GuavaModule
import scala.collection.JavaConversions._

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:38 PM
 * TODO: how to push to common config
 */
@Configuration
class DefaultScalaAppConfig/* extends DefaultAppConfg*/ {

  @Bean @Scope("singleton")
  def deadlockHealthCheck() = new DeadlockHealthCheck()

  @Bean @Scope("singleton")
  def healthChecks(healthChecks: JList[HealthCheck]) = {
    healthChecks foreach { healthCheck =>
      HealthChecks.register(healthCheck)
    }
    HealthChecks.defaultRegistry()
  }

  @Bean @Scope("singleton")
  def jacksonScalaCollections() = new ScalaCollectionsQueryParamInjectableProvider()

  @Bean @Scope("singleton")
  def scalaModule() = new ScalaModule(Thread.currentThread().getContextClassLoader)

  @Bean @Scope("singleton")
  def guavaModule() = new GuavaModule

  @Bean @Scope("singleton")
  def guavaExtrasModule() = new GuavaExtrasModule

  @Bean @Scope("singleton")
  def objectMapperProvider() = new HalfpipeScalaObjectMapperProvider(scalaModule(), guavaModule(),
    guavaExtrasModule(), annotationSensitivePropertyNamingStrategy)

  @Bean @Scope("singleton")
  def annotationSensitivePropertyNamingStrategy: AnnotationSensitivePropertyNamingStrategy =
    return new AnnotationSensitivePropertyNamingStrategy
}
