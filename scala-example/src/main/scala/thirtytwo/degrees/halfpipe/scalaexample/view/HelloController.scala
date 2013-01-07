package thirtytwo.degrees.halfpipe.scalaexample.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import collection.JavaConverters._
import javax.inject.Inject
import thirtytwo.degrees.halfpipe.scalaexample.ExampleScalaConfig
import thirtytwo.degrees.halfpipe.logging.Logging

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
