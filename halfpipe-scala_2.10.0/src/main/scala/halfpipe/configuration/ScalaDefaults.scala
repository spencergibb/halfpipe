package halfpipe.configuration

import com.netflix.config.scala._

/**
 * User: spencergibb
 * Date: 4/8/13
 * Time: 11:58 AM
 */
object ScalaDefaults {
  val DUMMY_PROP_NAME: String = "____dummy_scala_prop_**____"

  def prop(prop:String) = new DynamicStringProperty(DUMMY_PROP_NAME, prop) with ScalaPropertyWrapper[String]

  def prop(prop:Int) = new DynamicIntProperty(DUMMY_PROP_NAME, prop) with ScalaPropertyWrapper[Int]

  def prop(prop:Boolean) = new DynamicBooleanProperty(DUMMY_PROP_NAME, prop) with ScalaPropertyWrapper[Boolean]

  def prop(prop:Long) = new DynamicLongProperty(DUMMY_PROP_NAME, prop) with ScalaPropertyWrapper[Long]

  def prop(prop:Float) = new DynamicFloatProperty(DUMMY_PROP_NAME, prop) with ScalaPropertyWrapper[Float]

  def prop(prop:Double) = new DynamicDoubleProperty(DUMMY_PROP_NAME, prop) with ScalaPropertyWrapper[Double]
}
