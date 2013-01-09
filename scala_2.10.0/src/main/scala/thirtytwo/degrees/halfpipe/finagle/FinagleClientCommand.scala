package thirtytwo.degrees.halfpipe.finagle

import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.{CliCommand, CliAvailabilityIndicator}
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import java.net.InetSocketAddress
import org.jboss.netty.util.CharsetUtil
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import thirtytwo.degrees.halfpipe.configuration.Configuration

/**
 * User: spencergibb
 * Date: 10/10/12
 * Time: 11:11 PM
 */
class FinagleClientCommand (config: Configuration) extends CommandMarker {

  @CliAvailabilityIndicator(Array("finagle client"))
  def isCommandAvailable = true

  class InvalidRequest extends Exception
  /**
   * Convert HTTP 4xx and 5xx class responses into Exceptions.
   */
  class HandleErrors extends SimpleFilter[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest, service: Service[HttpRequest, HttpResponse]) = {
      // flatMap asynchronously responds to requests and can "map" them to both
      // success and failure values:
      service(request) flatMap { response =>
        response.getStatus match {
          case OK        => Future.value(response)
          case FORBIDDEN => Future.exception(new InvalidRequest)
          case _         => Future.exception(new Exception(response.getStatus.getReasonPhrase))
        }
      }
    }
  }

  @CliCommand(value = Array("finagle client"), help = "run finagle client")
  def finagleClient(): String = {
    val clientWithoutErrorHandling: Service[HttpRequest, HttpResponse] = ClientBuilder()
      .codec(Http())
      .hosts(new InetSocketAddress(config.http.port.get()))
      .hostConnectionLimit(1)
      .build()

    val handleErrors = new HandleErrors

    // compose the Filter with the client:
    val client: Service[HttpRequest, HttpResponse] = handleErrors andThen clientWithoutErrorHandling

    val authorizedRequest = new DefaultHttpRequest(
      HttpVersion.HTTP_1_1, HttpMethod.GET, "/hello")

    val req = client(authorizedRequest) onSuccess { response =>
      val responseString = response.getContent.toString(CharsetUtil.UTF_8)
      println("))) Received result for hello request: " + responseString)
    }
    req.apply()

    /*println("))) Issuing two requests in parallel: ")
    val request1 = makeAuthorizedRequest(client)
    val request2 = makeUnauthorizedRequest(client)

    // When both request1 and request2 have completed, close the TCP connection(s).
    val requests = (request1 join request2) ensure {
      client.release()
    }
    try {
      requests.apply()
    } catch {
      case e: Exception => {
        println("caught "+e.getClass+" "+e.getMessage)
      }
    }*/

    return null
  }

  private[this] def makeAuthorizedRequest(client: Service[HttpRequest, HttpResponse]) = {
    val authorizedRequest = new DefaultHttpRequest(
      HttpVersion.HTTP_1_1, HttpMethod.GET, "/")
    authorizedRequest.addHeader("Authorization", "open sesame")

    client(authorizedRequest) onSuccess { response =>
      val responseString = response.getContent.toString(CharsetUtil.UTF_8)
      println("))) Received result for authorized request: " + responseString)
    }
  }

  private[this] def makeUnauthorizedRequest(client: Service[HttpRequest, HttpResponse]) = {
    val unauthorizedRequest = new DefaultHttpRequest(
      HttpVersion.HTTP_1_1, HttpMethod.GET, "/")

    // use the onFailure callback since we convert HTTP 4xx and 5xx class
    // responses to Exceptions.
    client(unauthorizedRequest) onFailure { error =>
      println("))) Unauthorized request errored (as desired): " + error.getClass.getName)
    }
  }
}
