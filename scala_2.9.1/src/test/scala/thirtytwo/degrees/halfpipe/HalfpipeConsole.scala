package thirtytwo.degrees.halfpipe

import scalaexample.resources.HelloResource
import thirtytwo.degrees.halfpipe.scalaexample.AppConfig
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import tools.nsc.MainGenericRunner
import tools.nsc.interpreter.ILoop._

object HalfpipeConsole {
  def main(args: Array[String]) {
    val ctx = new AnnotationConfigApplicationContext(classOf[AppConfig])
    val hello = ctx.getBean(classOf[HelloResource])
    println(hello.hellotext(new Some[String](" here is more")))
    break(Nil)
    /*MainGenericRunner.main(args)
    sys.exit(0)*/
  }
}
