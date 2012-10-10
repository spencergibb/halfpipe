package thirtytwo.degrees.halfpipe.scalaexample.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import collection.JavaConverters._
import javax.inject.Inject
import thirtytwo.degrees.halfpipe.scalaexample.ExampleScalaConfig

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:19 PM
 */
@Controller
class HelloController @Inject() (config: ExampleScalaConfig) {

  @RequestMapping(Array("/hello"))
  def hello() = {
    println("HelloController: Passing through..."+config)
    val halloVal = "hello controller "+config.appName.get()
    Map("hello" -> halloVal) asJava
  }
}
