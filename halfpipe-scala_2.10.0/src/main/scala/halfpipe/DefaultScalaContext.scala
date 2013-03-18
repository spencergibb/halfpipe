package halfpipe

import configuration.ScalaConfigBuilder
import context.BaseContext
import inject.ScalaCollectionsQueryParamInjectableProvider
import org.springframework.context.annotation.{Import, Scope, Bean, Configuration}
import org.springframework.core.convert.ConversionService
import com.fasterxml.jackson.module.scala.DefaultScalaModule

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
  def scalaModule() = new DefaultScalaModule

  @Bean @Scope("singleton")
  def scalaConfigBuilder(conversionService:ConversionService) = new ScalaConfigBuilder(conversionService)
}
