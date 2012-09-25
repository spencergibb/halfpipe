package thirtytwo.degrees.halfpipe.jersey

import org.codehaus.jackson.map.{SerializationConfig, DeserializationConfig, ObjectMapper}
import javax.ws.rs.ext.{Provider, ContextResolver}
import com.codahale.jerkson.ScalaModule
import javax.inject.Inject
import com.fasterxml.jackson.datatype.guava.GuavaModule

/**
 * User: spencergibb
 * Date: 9/22/12
 * Time: 10:18 PM
 *
 * see http://nurkiewicz.blogspot.com/2011/09/evolution-of-spring-dependency.html
 *  for scala constructor injection
 */
@Provider
class HalfpipeScalaObjectMapperProvider (scalaModule: ScalaModule,
                                    guavaModule: GuavaModule,
                                    guavaExtrasModule: GuavaExtrasModule,
                                    namingStrategy: AnnotationSensitivePropertyNamingStrategy) extends ContextResolver[ObjectMapper] {

  private val objectMapper = {
    val mapper = ObjectMapperFactory.get(namingStrategy, guavaExtrasModule, guavaModule)
    mapper.registerModule(scalaModule)
    mapper
  }

  def getContext(klass: Class[_]) = {
    objectMapper
  }
}
