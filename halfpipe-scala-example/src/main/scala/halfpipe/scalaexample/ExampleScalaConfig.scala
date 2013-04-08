package halfpipe.scalaexample

import halfpipe.configuration.ScalaDefaults._
import halfpipe.configuration._
import com.netflix.config.scala.{DynamicStringProperty => DString}
import com.netflix.config.scala.{DynamicIntProperty => DInt}
import org.springframework.stereotype.Component

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

  @PropertyCallback(classOf[HelloIntCallback])
  var helloInt: DInt = prop(2000)
}

