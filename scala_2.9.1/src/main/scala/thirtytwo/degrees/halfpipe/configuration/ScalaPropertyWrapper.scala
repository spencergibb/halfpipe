package thirtytwo.degrees.halfpipe.configuration

/**
 * User: spencergibb
 * Date: 12/6/12
 * Time: 4:02 PM
 */
trait ScalaPropertyWrapper[V] {
  def apply(): Option[V]

  def get(): V

  def addCallback(callback: Runnable)
}
