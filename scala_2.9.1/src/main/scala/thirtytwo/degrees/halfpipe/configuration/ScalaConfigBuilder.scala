package thirtytwo.degrees.halfpipe.configuration

import org.springframework.util.ReflectionUtils
import org.springframework.util.ReflectionUtils.FieldCallback
import java.lang.reflect.Field
import com.netflix.config.scala.DynamicStringProperty
import javax.ws.rs.DefaultValue

/**
 * User: gibbsb
 * Date: 10/5/12
 * Time: 2:37 PM
 */
class ScalaConfigBuilder extends ConfigurationBuilder {
  protected override def build(config: Object, context: String) {
    val configClass = config.getClass

    ReflectionUtils.doWithFields(configClass, new FieldCallback {
      def doWith(field: Field) {
        val fieldType = field.getType
        val propName = getPropName(field, context)

        if (classOf[DynamicStringProperty].isAssignableFrom(fieldType)) {
          var defaultValue:String = null

          val annotation = field.getAnnotation(classOf[DefaultValue])
          if (annotation != null) {
            defaultValue = annotation.value()
          } else {
            defaultValue = ""
          }

          field.setAccessible(true)
          field.set(config, new DynamicStringProperty(propName, defaultValue))
        } else  if (field.get(config) == null) {
          val fieldConfig: Object = fieldType.newInstance().asInstanceOf[Object]
          build(fieldConfig, propName)
          field.set(config, fieldConfig)
        }
      }
    })
  }
}
