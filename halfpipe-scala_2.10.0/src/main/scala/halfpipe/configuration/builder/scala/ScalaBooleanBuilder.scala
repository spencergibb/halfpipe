package halfpipe.configuration.builder.scala

import halfpipe.configuration.builder.PropBuilder
import com.netflix.config.scala.DynamicBooleanProperty
import halfpipe.configuration.ScalaPropertyWrapper
import org.springframework.stereotype.Component

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 2:18 PM
 */
@Component
class ScalaBooleanBuilder extends PropBuilder[DynamicBooleanProperty, Boolean] {
  def getPropType = classOf[DynamicBooleanProperty]
  def defaultVal() = false
  def getProp(propName: String, defaultVal: Boolean, valueClass:Class[_]) =
    new DynamicBooleanProperty(propName, defaultVal) with ScalaPropertyWrapper[Boolean]
}