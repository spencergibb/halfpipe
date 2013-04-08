package halfpipe.configuration

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
    def getProp(propName: String, defaultVal: String, valueClass:Class[_]) =
      new DynamicStringProperty(propName, defaultVal) with ScalaPropertyWrapper[String]
  }

  class IntBuilder extends PropBuilder[DynamicIntProperty, Int] {
    def getPropType = classOf[DynamicIntProperty]
    def defaultVal() = Int.MinValue
    def getProp(propName: String, defaultVal: Int, valueClass:Class[_]) =
      new DynamicIntProperty(propName, defaultVal) with ScalaPropertyWrapper[Int]
  }

  class BooleanBuilder extends PropBuilder[DynamicBooleanProperty, Boolean] {
    def getPropType = classOf[DynamicBooleanProperty]
    def defaultVal() = false
    def getProp(propName: String, defaultVal: Boolean, valueClass:Class[_]) =
      new DynamicBooleanProperty(propName, defaultVal) with ScalaPropertyWrapper[Boolean]
  }

  class LongBuilder extends PropBuilder[DynamicLongProperty, Long] {
    def getPropType = classOf[DynamicLongProperty]
    def defaultVal() = Long.MinValue
    def getProp(propName: String, defaultVal: Long, valueClass:Class[_]) =
      new DynamicLongProperty(propName, defaultVal) with ScalaPropertyWrapper[Long]
  }

  class FloatBuilder extends PropBuilder[DynamicFloatProperty, Float] {
    def getPropType = classOf[DynamicFloatProperty]
    def defaultVal() = Float.NaN
    def getProp(propName: String, defaultVal: Float, valueClass:Class[_]) =
      new DynamicFloatProperty(propName, defaultVal) with ScalaPropertyWrapper[Float]
  }

  class DoubleBuilder extends PropBuilder[DynamicDoubleProperty, Double] {
    def getPropType = classOf[DynamicDoubleProperty]
    def defaultVal() = Double.NaN
    def getProp(propName: String, defaultVal: Double, valueClass:Class[_]) =
      new DynamicDoubleProperty(propName, defaultVal) with ScalaPropertyWrapper[Double]
  }

  builders.add(new StringBuilder)
  builders.add(new IntBuilder)
  builders.add(new BooleanBuilder)
  builders.add(new LongBuilder)
  builders.add(new FloatBuilder)
  builders.add(new DoubleBuilder)

  @SuppressWarnings(Array("unchecked"))
  protected override def addCallback(config: Any, prop: Any, propertyCallback: PropertyCallback) {
    if (propertyCallback == null)
      return

    val callbackClass = propertyCallback.value()

    if (prop.isInstanceOf[ScalaPropertyWrapper[_]]) {
      val property = prop.asInstanceOf[ScalaPropertyWrapper[_]]

      val callback = callbackClass.newInstance()

      if (callback.isInstanceOf[AbstractScalaCallback[Configuration, _]]) {
        val scalaCallback = callback.asInstanceOf[AbstractScalaCallback[Configuration, _]]
        scalaCallback.config = config.asInstanceOf[Configuration]
        scalaCallback.setProp(property)
        property.addCallback(scalaCallback)
      } else if (callback.isInstanceOf[Runnable]) {
        property.addCallback(callback.asInstanceOf[Runnable])
      } else {
        System.err.println("unknown callback class: "+callbackClass); //TODO: replace with logging
      }

    } else {
      super.addCallback(config, prop, propertyCallback)
    }
  }


  protected override def getDefaultValue(fieldValue: Any):AnyRef = {
    if (fieldValue != null && fieldValue.isInstanceOf[ScalaPropertyWrapper[_]]) {
      val property = fieldValue.asInstanceOf[ScalaPropertyWrapper[_]]
      val value: Any = property.get()
      if (value != null)
        return value.asInstanceOf[AnyRef]
    }
    super.getDefaultValue(fieldValue)
  }
}
