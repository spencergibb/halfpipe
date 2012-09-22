package thirtytwo.degrees.halfpipe.scalaexample.resources

import javax.ws.rs.{GET, Path}
import org.springframework.stereotype.Component
import javax.inject.{Named, Inject}
import com.netflix.config.{DynamicStringProperty => DString}

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 4:06 PM
 */
@Component
@Path ("/hello")
class HelloResource {

  @Inject @Named("helloText")
  var helloText: DString = _

  @GET
  def hello() = helloText.get()
}
