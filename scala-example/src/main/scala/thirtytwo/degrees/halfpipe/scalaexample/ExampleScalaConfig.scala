package thirtytwo.degrees.halfpipe.scalaexample

import thirtytwo.degrees.halfpipe.configuration._
import com.netflix.config.scala.{DynamicStringProperty => DString}
import com.netflix.config.scala.{DynamicIntProperty => DInt}
import org.springframework.stereotype.Component
import javax.ws.rs.DefaultValue

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 2:12 PM
 */

class HelloIntCallback extends AbstractScalaCallback[ExampleScalaConfig, Int]
{
  def run() {
    println("HelloIntCallback property changed to = "+ prop.get())
  }
}

@Component
class ExampleScalaConfig extends Configuration {
  var helloText: DString = _

  @DefaultValue("2000")
  @PropertyCallback(classOf[HelloIntCallback])
  var helloInt: DInt = _
}

