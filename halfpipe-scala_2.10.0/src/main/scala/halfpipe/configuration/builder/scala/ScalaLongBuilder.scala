package halfpipe.configuration.builder.scala

import halfpipe.configuration.builder.PropBuilder
import com.netflix.config.scala.DynamicLongProperty
import halfpipe.configuration.ScalaPropertyWrapper
import org.springframework.stereotype.Component

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 2:19 PM
 */
@Component
class ScalaLongBuilder extends PropBuilder[DynamicLongProperty, Long] {
  def getPropType = classOf[DynamicLongProperty]
  def defaultVal() = Long.MinValue
  def getProp(propName: String, defaultVal: Long, valueClass:Class[_]) =
    new DynamicLongProperty(propName, defaultVal) with ScalaPropertyWrapper[Long]
}