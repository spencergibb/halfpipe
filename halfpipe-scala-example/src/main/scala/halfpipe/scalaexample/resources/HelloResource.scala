package halfpipe.scalaexample.resources

import javax.ws.rs.{Produces, QueryParam, GET, Path}
import org.springframework.stereotype.Component
import javax.inject.Inject
import com.netflix.config.scala.{DynamicStringProperty => DString}
import halfpipe.scalaexample.core.Hello
import javax.ws.rs.core.MediaType
import com.yammer.metrics.annotation.Timed
import halfpipe.scalaexample.ExampleScalaConfig
import org.springframework.beans.factory.annotation.Autowired

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:06 PM
 *
 * see http://nurkiewicz.blogspot.com/2011/09/evolution-of-spring-dependency.html
 *  for scala constructor injection
 */
@Component
@Path("/hello")
class HelloResource @Inject() (config: ExampleScalaConfig) {

  // TODO: spring proxies break resources in scala
  @GET
  @Path("/text")
  @Produces(Array(MediaType.TEXT_PLAIN))
  def hellotext(@QueryParam("name") name: Option[String]) =
    config.helloText.get() + name.getOrElse("")

  @Timed
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def hellojson(@QueryParam("more") more: Option[String]) =
    new Hello(config.helloText.get(), "Scala Resource"+more.getOrElse(""))
}
