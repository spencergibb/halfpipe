package halfpipe.logging

import org.slf4j.Logger

/**
 * User: spencergibb
 * Date: 12/7/12
 * Time: 10:38 AM
 */
trait Logging {

  protected lazy val log: Logger = Log.forClass(getClass)
}
