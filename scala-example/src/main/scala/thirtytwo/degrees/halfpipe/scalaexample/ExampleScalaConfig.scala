package thirtytwo.degrees.halfpipe.scalaexample

import thirtytwo.degrees.halfpipe.configuration.Configuration
import com.netflix.config.scala.{DynamicStringProperty => DString}
import com.netflix.config.scala.{DynamicIntProperty => DInt}
import org.springframework.stereotype.Component
import javax.ws.rs.DefaultValue

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 2:12 PM
 */
@Component
class ExampleScalaConfig extends Configuration {
  var helloText: DString = _

  @DefaultValue("2000")
  var helloInt: DInt = _
}
