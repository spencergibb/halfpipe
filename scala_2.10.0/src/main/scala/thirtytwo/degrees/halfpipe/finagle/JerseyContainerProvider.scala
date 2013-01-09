package thirtytwo.degrees.halfpipe.finagle

import com.sun.jersey.spi.container.{WebApplication, ContainerProvider}
import com.sun.jersey.api.core.ResourceConfig

/**
 * User: spencergibb
 * Date: 10/11/12
 * Time: 12:51 AM
 */
class JerseyContainerProvider extends ContainerProvider[JerseyContainer] {
  def createContainer(containerType: Class[JerseyContainer],
                      resourceConfig: ResourceConfig,
                      application: WebApplication): JerseyContainer = {
    if (containerType.equals(classOf[JerseyContainer]))
      return new JerseyContainer(application, resourceConfig)
    return null
  }
}
