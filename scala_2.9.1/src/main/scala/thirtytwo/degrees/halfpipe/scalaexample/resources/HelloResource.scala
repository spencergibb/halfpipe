package thirtytwo.degrees.halfpipe.scalaexample.resources

import javax.ws.rs.{Produces, QueryParam, GET, Path}
import org.springframework.stereotype.Component
import javax.inject.{Named, Inject}
import com.netflix.config.scala.{DynamicStringProperty => DString}
import thirtytwo.degrees.halfpipe.scalaexample.core.Hello
import javax.ws.rs.core.MediaType
import com.yammer.metrics.annotation.Timed

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 4:06 PM
 *
 * see http://nurkiewicz.blogspot.com/2011/09/evolution-of-spring-dependency.html
 *  for scala constructor injection
 */
@Component
@Path("/hello")
class HelloResource @Inject() (@Named("helloText") helloText: DString) {

  @GET
  @Path("/text")
  @Produces(Array(MediaType.TEXT_PLAIN))
  @Timed
  def hellotext(@QueryParam("more") more: Option[String]) =
    helloText.get() + more.getOrElse("")

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Timed
  def hellojson(@QueryParam("more") more: Option[String]) =
    new Hello(helloText.get(), "Scala Resource"+more.getOrElse(""))
}
