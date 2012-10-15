package thirtytwo.degrees.halfpipe.configuration

import com.netflix.config.scala._
import org.springframework.core.convert.ConversionService

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 2:37 PM
 */
class ScalaConfigBuilder(conversionService: ConversionService) extends ConfigurationBuilder(conversionService) {

  class StringBuilder extends PropBuilder[DynamicStringProperty, String] {
    def getPropType = classOf[DynamicStringProperty]
    def defaultVal() = ""
    def getProp(propName: String, defaultVal: String, valueClass:Class[_]) = new DynamicStringProperty(propName, defaultVal)
  }

  class IntBuilder extends PropBuilder[DynamicIntProperty, Int] {
    def getPropType = classOf[DynamicIntProperty]
    def defaultVal() = Int.MinValue
    def getProp(propName: String, defaultVal: Int, valueClass:Class[_]) = new DynamicIntProperty(propName, defaultVal)
  }

  class BooleanBuilder extends PropBuilder[DynamicBooleanProperty, Boolean] {
    def getPropType = classOf[DynamicBooleanProperty]
    def defaultVal() = false
    def getProp(propName: String, defaultVal: Boolean, valueClass:Class[_]) = new DynamicBooleanProperty(propName, defaultVal)
  }

  class LongBuilder extends PropBuilder[DynamicLongProperty, Long] {
    def getPropType = classOf[DynamicLongProperty]
    def defaultVal() = Long.MinValue
    def getProp(propName: String, defaultVal: Long, valueClass:Class[_]) = new DynamicLongProperty(propName, defaultVal)
  }

  class FloatBuilder extends PropBuilder[DynamicFloatProperty, Float] {
    def getPropType = classOf[DynamicFloatProperty]
    def defaultVal() = Float.NaN
    def getProp(propName: String, defaultVal: Float, valueClass:Class[_]) = new DynamicFloatProperty(propName, defaultVal)
  }

  class DoubleBuilder extends PropBuilder[DynamicDoubleProperty, Double] {
    def getPropType = classOf[DynamicDoubleProperty]
    def defaultVal() = Double.NaN
    def getProp(propName: String, defaultVal: Double, valueClass:Class[_]) = new DynamicDoubleProperty(propName, defaultVal)
  }

  builders.add(new StringBuilder)
  builders.add(new IntBuilder)
  builders.add(new BooleanBuilder)
  builders.add(new LongBuilder)
  builders.add(new FloatBuilder)
  builders.add(new DoubleBuilder)
}
