package halfpipe.configuration.builder.scala

import org.springframework.stereotype.Component
import halfpipe.configuration.builder.PropBuilder
import com.netflix.config.scala.DynamicIntProperty
import halfpipe.configuration.ScalaPropertyWrapper

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 2:17 PM
 */
@Component
class ScalaIntBuilder extends PropBuilder[DynamicIntProperty, Int] {
  def getPropType = classOf[DynamicIntProperty]
  def defaultVal() = Int.MinValue
  def getProp(propName: String, defaultVal: Int, valueClass:Class[_]) =
    new DynamicIntProperty(propName, defaultVal) with ScalaPropertyWrapper[Int]
}
