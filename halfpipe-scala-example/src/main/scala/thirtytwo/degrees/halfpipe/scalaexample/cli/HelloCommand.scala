package thirtytwo.degrees.halfpipe.scalaexample.cli

import javax.inject.{Named, Inject}
import com.netflix.config.scala.{DynamicStringProperty => DString}
import org.springframework.shell.core.CommandMarker
import org.apache.commons.lang.StringUtils
import org.springframework.shell.core.annotation.{CliOption, CliCommand, CliAvailabilityIndicator}
import org.springframework.stereotype.Component
import thirtytwo.degrees.halfpipe.scalaexample.ExampleScalaConfig

/**
 * User: spencergibb
 * Date: 10/2/12
 * Time: 9:53 PM
 */
@Component
class HelloCommand @Inject() (config: ExampleScalaConfig) extends CommandMarker {

  @CliAvailabilityIndicator(Array("hello"))
  def isCommandAvailable = true

  //TODO: write a scala dsl instead of shell annotations
  @CliCommand(value = Array("hello"), help = "Say hello to the shell")
  def hello(
             @CliOption(key = Array("message"), mandatory = true, help = "The hello world message")
             message: String,
             @CliOption(key = Array("more"), mandatory = false, help = "Have some more to say while saying hello",
             specifiedDefaultValue = "") more: String): String =
  {
    val hello: StringBuilder = new StringBuilder(config.helloText.get())
    hello.append(" with this message, " + message)
    if (!StringUtils.isBlank(more)) {
      hello.append(" " + more)
    }
    return hello.toString()
  }
}
