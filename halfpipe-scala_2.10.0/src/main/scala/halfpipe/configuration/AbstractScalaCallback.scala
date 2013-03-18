package halfpipe.configuration

/**
 * User: spencergibb
 * Date: 3/16/13
 * Time: 8:55 PM
 */
abstract class AbstractScalaCallback[C, V]() extends Runnable {
  var config: C = _
  var prop: ScalaPropertyWrapper[V] = _

  def setProp(p: Any) {
    prop = p.asInstanceOf[ScalaPropertyWrapper[V]]
  }
}
