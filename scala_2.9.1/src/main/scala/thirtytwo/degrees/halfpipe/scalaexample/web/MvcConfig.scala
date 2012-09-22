package thirtytwo.degrees.halfpipe.scalaexample.web

import org.springframework.web.servlet.config.annotation.{ResourceHandlerRegistry, WebMvcConfigurerAdapter, EnableWebMvc}
import org.springframework.context.annotation.{ComponentScan, Configuration}
import thirtytwo.degrees.halfpipe.web.admin.AdminControllers

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 4:11 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = Array(classOf[MvcConfig], classOf[AdminControllers]))
class MvcConfig extends WebMvcConfigurerAdapter {

  override def addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
  }
}
