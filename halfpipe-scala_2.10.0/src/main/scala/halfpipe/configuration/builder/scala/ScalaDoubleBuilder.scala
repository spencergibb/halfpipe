package halfpipe.configuration.builder.scala

import halfpipe.configuration.builder.PropBuilder
import com.netflix.config.scala.DynamicDoubleProperty
import halfpipe.configuration.ScalaPropertyWrapper

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 2:22 PM
 */
class ScalaDoubleBuilder extends PropBuilder[DynamicDoubleProperty, Double] {
  def getPropType = classOf[DynamicDoubleProperty]
  def defaultVal() = Double.NaN
  def getProp(propName: String, defaultVal: Double, valueClass:Class[_]) =
    new DynamicDoubleProperty(propName, defaultVal) with ScalaPropertyWrapper[Double]
}