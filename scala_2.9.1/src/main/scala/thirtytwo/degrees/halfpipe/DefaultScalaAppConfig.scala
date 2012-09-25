package thirtytwo.degrees.halfpipe

import config.DefaultAppConfg
import org.springframework.context.annotation.{Import, Scope, Bean, Configuration}
import com.codahale.jerkson.ScalaModule
import com.codahale.jersey.inject.ScalaCollectionsQueryParamInjectableProvider

/**
 * User: spencergibb
 * Date: 9/24/12
 * Time: 4:38 PM
 */
@Configuration
@Import(Array(classOf[DefaultAppConfg]))
class DefaultScalaAppConfig {

  @Bean @Scope("singleton")
  def jacksonScalaCollections() = new ScalaCollectionsQueryParamInjectableProvider()

  @Bean @Scope("singleton")
  def scalaModule() = new ScalaModule(Thread.currentThread().getContextClassLoader)

}
