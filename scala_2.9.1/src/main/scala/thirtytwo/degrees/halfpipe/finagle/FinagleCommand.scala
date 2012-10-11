package thirtytwo.degrees.halfpipe.finagle

import thirtytwo.degrees.halfpipe.HalfpipeConfiguration._
import scala.collection.JavaConverters._
import org.springframework.shell.core.annotation.{CliCommand, CliAvailabilityIndicator}
import org.jboss.netty.handler.codec.http.HttpVersion._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.http.Http
import com.twitter.finagle.builder.{Server, ServerBuilder}
import java.net.{URI, InetSocketAddress}
import org.jboss.netty.handler.codec.http.{HttpHeaders, HttpResponse, HttpRequest, DefaultHttpResponse}
import com.twitter.util.Future
import com.twitter.finagle.{SimpleFilter, Service}
import org.springframework.shell.core.CommandMarker
import thirtytwo.degrees.halfpipe.configuration.Configuration
import com.sun.jersey.api.container.ContainerFactory
import com.sun.jersey.spi.container.ContainerRequest
import thirtytwo.degrees.halfpipe.jersey.HalfpipeResourceConfig
import org.springframework.context.{ConfigurableApplicationContext, ApplicationContext}
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory
import com.sun.jersey.core.header.InBoundHeaders
import org.jboss.netty.buffer.ChannelBufferInputStream

/**
 * User: spencergibb
 * Date: 10/10/12
 * Time: 6:26 PM
 */
class FinagleCommand (config: Configuration, context: ApplicationContext) extends CommandMarker {

  @CliAvailabilityIndicator(Array("finagle server"))
  def isCommandAvailable = true

  /**
   * A simple Filter that catches exceptions and converts them to appropriate
   * HTTP responses.
   */
  class HandleExceptions extends SimpleFilter[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest, service: Service[HttpRequest, HttpResponse]) = {

      // `handle` asynchronously handles exceptions.
      service(request) handle { case error =>
        val statusCode = error match {
          case _: IllegalArgumentException =>
            FORBIDDEN
          case _ =>
            INTERNAL_SERVER_ERROR
        }
        val errorResponse = new DefaultHttpResponse(HTTP_1_1, statusCode)
        errorResponse.setContent(copiedBuffer(error.getStackTraceString, UTF_8))

        errorResponse
      }
    }
  }

  /**
   * A simple Filter that checks that the request is valid by inspecting the
   * "Authorization" header.
   */
  class Authorize extends SimpleFilter[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest, continue: Service[HttpRequest, HttpResponse]) = {
      if ("open sesame" == request.getHeader("Authorization")) {
        println("received valid auth request")
        continue(request)
      } else {
        println("received failed auth request")
        Future.exception(new IllegalArgumentException("You don't know the secret"))
      }
    }
  }

  /**
   * The service itself. Simply echos back "hello world"
   */
  class Respond extends Service[HttpRequest, HttpResponse] {
    // https://github.com/devsprint/jersey-netty-container
    val container = {
      val resourceConfig = new HalfpipeResourceConfig(jerseyProperties(config))
      val factory = new SpringComponentProviderFactory(resourceConfig, context.asInstanceOf[ConfigurableApplicationContext])
      ContainerFactory.createContainer(classOf[JerseyContainer], resourceConfig,
        factory)
    }

    def apply(request: HttpRequest) = {
      val baseUri = getBaseUri(request)
      val requestUri = baseUri.substring(0, baseUri.length-1)+request.getUri
      val creq = new ContainerRequest(container.webApplication, request.getMethod.getName,
            new URI(baseUri), new URI(requestUri), getHeaders(request),
            new ChannelBufferInputStream(request.getContent)
      )

      val response = new DefaultHttpResponse(HTTP_1_1, OK)

      container.webApplication.handleRequest(creq, new JerseyResponseWriter(response))
      //response.setContent(copiedBuffer("hello world", UTF_8))
      Future.value(response)
    }
  }

  @CliCommand(value = Array("finagle server"), help = "run finagle server")
  def finagleServer(): String = {
    val handleExceptions = new HandleExceptions
    val authorize = new Authorize
    val respond = new Respond

    // compose the Filters and Service together:
    val myService: Service[HttpRequest, HttpResponse] = handleExceptions /*andThen authorize*/ andThen respond

    val server: Server = ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(config.http.port.get()))
      .name("httpserver")
      .build(myService)

    waitIndefinitely
    return null
  }

  def getHeaders(request: HttpRequest) = {
    val headers = new InBoundHeaders()

    request.getHeaderNames.asScala.foreach { name =>
      headers.put(name, request.getHeaders(name))
    }

    headers
  }

  def getBaseUri(request: HttpRequest) = {
    //TODO: https
    "http://" + request.getHeader(HttpHeaders.Names.HOST) + "/"
  }

  private def waitIndefinitely {
    val lock: AnyRef = new AnyRef
    lock synchronized {
      try {
        lock.wait
      }
      catch {
        case exception: InterruptedException => {
          throw new Error("InterruptedException on wait Indefinitely lock:" + exception.getMessage, exception)
        }
      }
    }
  }
}
