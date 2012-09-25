package thirtytwo.degrees.halfpipe.scalaexample.view

import org.springframework.context.annotation.{Import, ComponentScan, Configuration}
import thirtytwo.degrees.halfpipe.config.DefaultViewConfg

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:11 PM
 */
@Configuration
@ComponentScan(basePackageClasses = Array(classOf[ViewConfig]))
@Import(Array(classOf[DefaultViewConfg]))
class ViewConfig {
}
