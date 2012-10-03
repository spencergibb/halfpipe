#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.cli

import javax.inject.{Named, Inject}
import com.netflix.config.scala.{DynamicStringProperty => DString}
import org.springframework.shell.core.CommandMarker
import org.apache.commons.lang.StringUtils
import org.springframework.shell.core.annotation.{CliOption, CliCommand, CliAvailabilityIndicator}
import org.springframework.stereotype.Component

@Component
class HelloCommand @Inject() (@Named("helloText") helloText: DString) extends CommandMarker {

  @CliAvailabilityIndicator(Array("hello"))
  def isCommandAvailable = {
    true
  }

  @CliCommand(value = Array("hello"), help = "Say hello to the shell")
  def hello(
             @CliOption(key = Array("message"), mandatory = true, help = "The hello world message")
             message: String,
             @CliOption(key = Array("more"), mandatory = false, help = "Have some more to say while saying hello",
             specifiedDefaultValue = "") more: String): String =
  {
    val hello: StringBuilder = new StringBuilder(helloText.get())
    hello.append(" with this message, " + message)
    if (!StringUtils.isBlank(more)) {
      hello.append(" " + more)
    }
    System.out.println("\n\n\n\n" + hello + "\n\n\n\n")
    return null
  }
}
