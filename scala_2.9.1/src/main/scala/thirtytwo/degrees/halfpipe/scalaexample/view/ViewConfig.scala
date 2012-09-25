package thirtytwo.degrees.halfpipe.scalaexample.view

import org.springframework.web.servlet.config.annotation.{ResourceHandlerRegistry, WebMvcConfigurerAdapter, EnableWebMvc}
import org.springframework.context.annotation.{ComponentScan, Configuration}
import thirtytwo.degrees.halfpipe.web.admin.AdminControllers

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:11 PM
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = Array(classOf[ViewConfig], classOf[AdminControllers]))
class ViewConfig extends WebMvcConfigurerAdapter {

  override def addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
  }
}
