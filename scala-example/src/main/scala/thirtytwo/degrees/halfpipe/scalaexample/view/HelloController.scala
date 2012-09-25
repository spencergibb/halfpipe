package thirtytwo.degrees.halfpipe.scalaexample.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import collection.JavaConverters._

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 4:19 PM
 */
@Controller
class HelloController {

  @RequestMapping(Array("/hello"))
  def hello() = {
    println("HelloController: Passing through...")
    Map("hello" -> "hello controller") asJava
  }
}
