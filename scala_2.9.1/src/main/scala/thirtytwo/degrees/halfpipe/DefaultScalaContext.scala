package thirtytwo.degrees.halfpipe

import configuration.ScalaConfigBuilder
import context.{BaseContext, DefaultContext}
import org.springframework.context.annotation.{Import, Scope, Bean, Configuration}
import com.codahale.jerkson.ScalaModule
import com.codahale.jersey.inject.ScalaCollectionsQueryParamInjectableProvider
import org.springframework.core.convert.ConversionService

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:38 PM
 */
@Configuration
@Import(Array(classOf[BaseContext]))
class DefaultScalaContext {

  @Bean @Scope("singleton")
  def jacksonScalaCollections() = new ScalaCollectionsQueryParamInjectableProvider()

  @Bean @Scope("singleton")
  def scalaModule() = new ScalaModule(Thread.currentThread().getContextClassLoader)

  @Bean @Scope("singleton")
  def scalaConfigBuilder(conversionService:ConversionService) = new ScalaConfigBuilder(conversionService)
}
