package halfpipe.configuration.builder.scala

import halfpipe.configuration.builder.PropBuilder
import com.netflix.config.scala.DynamicFloatProperty
import halfpipe.configuration.ScalaPropertyWrapper
import org.springframework.stereotype.Component

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 2:20 PM
 */
@Component
class ScalaFloatBuilder extends PropBuilder[DynamicFloatProperty, Float] {
  def getPropType = classOf[DynamicFloatProperty]
  def defaultVal() = Float.NaN
  def getProp(propName: String, defaultVal: Float, valueClass:Class[_]) =
    new DynamicFloatProperty(propName, defaultVal) with ScalaPropertyWrapper[Float]
}