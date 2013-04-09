package halfpipe.configuration

import java.util.{List => JList}
import builder.PropBuilder
import org.springframework.core.convert.ConversionService

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 2:37 PM
 */
class ScalaConfigBuilder(conversionService: ConversionService, builders:JList[PropBuilder[_, _]])
  extends ConfigurationBuilder(conversionService, builders)
{

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
