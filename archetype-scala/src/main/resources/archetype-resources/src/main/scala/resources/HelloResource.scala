#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.resources

import javax.ws.rs.{Produces, QueryParam, GET, Path}
import org.springframework.stereotype.Component
import javax.inject.{Named, Inject}
import com.netflix.config.scala.{DynamicStringProperty => DString}
import ${package}.core.Hello
import javax.ws.rs.core.MediaType
import com.yammer.metrics.annotation.Timed

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
