package thirtytwo.degrees.halfpipe.scalaexample.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.Writer

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 4:19 PM
 */
@Controller
class HelloController {

  @RequestMapping(Array("/hello"))
  def hello(out: Writer) = {
    println("HelloController: Passing through...")
    out.write("hello controller")
    out.flush()
    out.close()
  }
}
