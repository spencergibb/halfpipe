package thirtytwo.degrees.halfpipe.jersey

import org.codehaus.jackson.map.ObjectMapper
import javax.ws.rs.ext.{Provider, ContextResolver}
import com.codahale.jerkson.ScalaModule
import javax.inject.Inject
import com.fasterxml.jackson.datatype.guava.GuavaModule

/**
 * User: gibbsb
 * Date: 9/22/12
 * Time: 10:18 PM
 *
 * see http://nurkiewicz.blogspot.com/2011/09/evolution-of-spring-dependency.html
 *  for scala constructor injection
 */
@Provider
class HalfpipeScalaObjectMapperProvider (scalaModule: ScalaModule,
                                    guavaModule: GuavaModule) extends ContextResolver[ObjectMapper] {

  def getContext(klass: Class[_]) = {
    val mapper = new ObjectMapper()
    mapper.registerModule(scalaModule)
    mapper.registerModule(guavaModule)
    mapper
  }
}
