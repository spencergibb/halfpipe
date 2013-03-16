package thirtytwo.degrees.halfpipe.scalaexample.view

import org.springframework.context.annotation.{Import, Bean, ComponentScan, Configuration}
import thirtytwo.degrees.halfpipe.context.{MetricsContext, AbstractViewContext}
import org.springframework.web.servlet.config.annotation.{ContentNegotiationConfigurer, ViewControllerRegistry, EnableWebMvc}
import org.springframework.web.servlet.view.freemarker.{FreeMarkerViewResolver, FreeMarkerConfigurer}
import thirtytwo.degrees.halfpipe.mgmt.view.MgmtControllers
import org.springframework.context.annotation.ComponentScan.Filter
import java.util
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import javax.inject.Inject
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:11 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = Array(classOf[ViewContext], classOf[MgmtControllers]),
  excludeFilters = Array(new Filter (Array (classOf[Configuration]))))
@Import(Array(classOf[MetricsContext]))
class ViewContext extends AbstractViewContext {

  /*@Bean
  def jspViewResolver = {
    val view = new UrlBasedViewResolver()
    view.setViewClass(classOf[JstlView])
    view.setPrefix("/WEB-INF/jsp/")
    view.setSuffix(".jsp")
    view
  }*/

  //TODO: https://github.com/sps/mustache-spring-view

  @Bean
  def freemarkerConfig = {
    val cfg = new FreeMarkerConfigurer
    cfg.setTemplateLoaderPath("classpath:freemarker")
    cfg
  }

  @Bean
  def freemarkerViewResolver = {
    val view = new FreeMarkerViewResolver
    view.setCache(true)
    view.setPrefix("")
    view.setSuffix(".ftl")
    view
  }

  @Inject
  var objectMapper: ObjectMapper = _

  @Bean
  def jacksonMessageConverter = {
    val conv = new MappingJackson2HttpMessageConverter()
    conv.setObjectMapper(objectMapper)
    conv
  }

  override def addViewControllers(registry: ViewControllerRegistry) {
    registry.addViewController("/").setViewName("index")
  }

  override def configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
    configurer.ignoreAcceptHeader(false)
  }

  override def configureMessageConverters(converters: util.List[HttpMessageConverter[_]]) {
    converters.add(jacksonMessageConverter)
  }
}
