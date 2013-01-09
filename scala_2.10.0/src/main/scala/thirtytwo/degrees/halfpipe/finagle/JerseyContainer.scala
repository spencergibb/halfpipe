package thirtytwo.degrees.halfpipe.finagle

import com.sun.jersey.spi.container.WebApplication
import com.sun.jersey.api.core.ResourceConfig

/**
 * User: spencergibb
 * Date: 10/11/12
 * Time: 12:53 AM
 */

class JerseyContainer(val webApplication: WebApplication, resourceConfig: ResourceConfig) {
  println("jerseycontainer")
}
