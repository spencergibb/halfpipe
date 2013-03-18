package halfpipe.scalaexample.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import javax.inject.Inject
import halfpipe.scalaexample.ExampleScalaConfig
import halfpipe.logging.Logging
import com.yammer.metrics.annotation.Timed
import halfpipe.scalaexample.core.Hello
import org.springframework.http.MediaType

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:19 PM
 */
@Controller
class HelloWS @Inject() (config: ExampleScalaConfig) extends Logging {

  //@Timed
  @RequestMapping(value = Array("/hellows"),
    produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @ResponseBody
  def helloserv():Hello = {
    new Hello(config.helloText.get(), "Scala Spring Controller")
  }
}
