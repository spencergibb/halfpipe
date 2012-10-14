package thirtytwo.degrees.halfpipe.configuration

import org.springframework.util.ReflectionUtils
import org.springframework.util.ReflectionUtils.FieldCallback
import java.lang.reflect.Field
import com.netflix.config.scala._
import scala.collection.immutable.StringOps
import javax.ws.rs.DefaultValue
import thirtytwo.degrees.halfpipe.configuration.ConfigurationBuilder.PropBuilder

/**
 * User: gibbsb
 * Date: 10/5/12
 * Time: 2:37 PM
 */
class ScalaConfigBuilder extends ConfigurationBuilder {

  class StringBuilder extends PropBuilder[DynamicStringProperty, String] {
    def getPropType = classOf[DynamicStringProperty]
    def defaultVal() = ""
    def convert(s: String, valueClass:Class[_]) = s
    def getProp(propName: String, defaultVal: String, valueClass:Class[_]) = new DynamicStringProperty(propName, defaultVal)
  }

  class IntBuilder extends PropBuilder[DynamicIntProperty, Int] {
    def getPropType = classOf[DynamicIntProperty]
    def defaultVal() = 0
    def convert(s: String, valueClass:Class[_]) = s.toInt
    def getProp(propName: String, defaultVal: Int, valueClass:Class[_]) = new DynamicIntProperty(propName, defaultVal)
  }

  class BooleanBuilder extends PropBuilder[DynamicBooleanProperty, Boolean] {
    def getPropType = classOf[DynamicBooleanProperty]
    def defaultVal() = false
    def convert(s: String, valueClass:Class[_]) = s.toBoolean
    def getProp(propName: String, defaultVal: Boolean, valueClass:Class[_]) = new DynamicBooleanProperty(propName, defaultVal)
  }

  class LongBuilder extends PropBuilder[DynamicLongProperty, Long] {
    def getPropType = classOf[DynamicLongProperty]
    def defaultVal() = 0
    def convert(s: String, valueClass:Class[_]) = s.toLong
    def getProp(propName: String, defaultVal: Long, valueClass:Class[_]) = new DynamicLongProperty(propName, defaultVal)
  }

  class FloatBuilder extends PropBuilder[DynamicFloatProperty, Float] {
    def getPropType = classOf[DynamicFloatProperty]
    def defaultVal() = 0.0f
    def convert(s: String, valueClass:Class[_]) = s.toFloat
    def getProp(propName: String, defaultVal: Float, valueClass:Class[_]) = new DynamicFloatProperty(propName, defaultVal)
  }

  class DoubleBuilder extends PropBuilder[DynamicDoubleProperty, Double] {
    def getPropType = classOf[DynamicDoubleProperty]
    def defaultVal() = 0.0d
    def convert(s: String, valueClass:Class[_]) = s.toDouble
    def getProp(propName: String, defaultVal: Double, valueClass:Class[_]) = new DynamicDoubleProperty(propName, defaultVal)
  }

  builders.add(new StringBuilder)
  builders.add(new IntBuilder)
  builders.add(new BooleanBuilder)
  builders.add(new LongBuilder)
  builders.add(new FloatBuilder)
  builders.add(new DoubleBuilder)
}
