package thirtytwo.degrees.halfpipe.logging

/**
 * User: spencergibb
 * Date: 12/7/12
 * Time: 10:38 AM
 */
trait Logging {

  protected lazy val log: Log = Log.forClass(getClass)
}
