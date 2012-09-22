package thirtytwo.degrees.halfpipe.scalaexample.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 4:17 PM
 */
@Controller
class IndexController {

  @RequestMapping(Array("/"))
  def home() = "forward:/index.html"
}
