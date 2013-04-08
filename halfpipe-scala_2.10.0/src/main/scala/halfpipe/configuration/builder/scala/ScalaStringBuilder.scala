package halfpipe.configuration.builder.scala

import halfpipe.configuration.builder.PropBuilder
import com.netflix.config.scala.DynamicStringProperty
import halfpipe.configuration.ScalaPropertyWrapper
import org.springframework.stereotype.Component

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 2:14 PM
 */
@Component
class ScalaStringBuilder extends PropBuilder[DynamicStringProperty, String] {
  def getPropType = classOf[DynamicStringProperty]
  def defaultVal() = ""
  def getProp(propName: String, defaultVal: String, valueClass:Class[_]) =
    new DynamicStringProperty(propName, defaultVal) with ScalaPropertyWrapper[String]
}