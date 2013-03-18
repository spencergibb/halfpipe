package halfpipe.scalaexample.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import collection.JavaConverters._
import javax.inject.Inject
import halfpipe.scalaexample.ExampleScalaConfig
import halfpipe.logging.Logging
import com.yammer.metrics.annotation.Timed

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:19 PM
 */
@Controller
class HelloController @Inject() (config: ExampleScalaConfig) extends Logging {

  @RequestMapping(Array("/hello"))
  def hello() = {
    log.info("HelloController: Passing through... {}, {}, {}", "arg1", "arg2", config.helloText.get())
    val halloVal = "hello controller "+config.appName.get()
    Map("hello" -> halloVal).asJava
  }

}
