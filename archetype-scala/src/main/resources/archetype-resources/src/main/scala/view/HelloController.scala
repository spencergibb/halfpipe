#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import collection.JavaConverters._

@Controller
class HelloController {

  @RequestMapping(Array("/hello"))
  def hello() = {
    println("HelloController: Passing through...")
    Map("hello" -> "hello controller") asJava
  }
}
