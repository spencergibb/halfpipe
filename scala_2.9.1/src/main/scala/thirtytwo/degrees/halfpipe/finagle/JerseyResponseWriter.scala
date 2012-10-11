package thirtytwo.degrees.halfpipe.finagle

import com.sun.jersey.spi.container.{ContainerResponse, ContainerResponseWriter}
import org.jboss.netty.channel.{ChannelFutureListener, Channel}
import org.jboss.netty.handler.codec.http.{HttpResponseStatus, HttpVersion, DefaultHttpResponse, HttpResponse}
import scala.collection.JavaConverters._
import org.jboss.netty.buffer.{ChannelBufferOutputStream, ChannelBuffers}

/**
 * User: spencergibb
 * Date: 10/11/12
 * Time: 1:18 AM
 */
class JerseyResponseWriter(val response: HttpResponse = null) extends ContainerResponseWriter {

  def writeStatusAndHeaders(contentLength: Long, containerResponse: ContainerResponse) = {
    response.setStatus(HttpResponseStatus.valueOf(containerResponse.getStatus()))

    containerResponse.getHttpHeaders.entrySet().asScala.foreach { header =>
      response.setHeader(header.getKey, header.getValue)
    }

    val buffer = ChannelBuffers.dynamicBuffer()
    response.setContent(buffer)
    new ChannelBufferOutputStream(buffer)
  }

  def finish() = {
    //channel.write(response).addListener(ChannelFutureListener.CLOSE)
  }

}