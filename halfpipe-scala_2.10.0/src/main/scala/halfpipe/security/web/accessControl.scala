package halfpipe.security.web

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.access.{AccessDecisionManager, AccessDecisionVoter, ConfigAttribute}
import org.springframework.security.access.vote.AffirmativeBased
import org.springframework.security.web.util.{AnyRequestMatcher, RequestMatcher}
import org.springframework.security.web.access.intercept._
import collection.immutable.ListMap
import java.{util => ju}
import ju.Arrays

import halfpipe.security._

import WebConversions._

/**
 * Trait which contains the `FilterSecurityInterceptor`, `AccessDecisionManager` and related beans.
 */
trait WebAccessControl extends EventPublisher {

  lazy val filterSecurityInterceptor = {
    val fsi = new FilterSecurityInterceptor()
    fsi.setSecurityMetadataSource(securityMetadataSource)
    fsi.setAccessDecisionManager(accessDecisionManager)
    fsi.setApplicationEventPublisher(eventPublisher)
    fsi
  }

  private var accessUrls : ListMap[RequestMatcher, ju.List[ConfigAttribute]] = ListMap()

  private def securityMetadataSource : FilterInvocationSecurityMetadataSource
          = new DefaultFilterInvocationSecurityMetadataSource(accessUrls)

  def interceptUrl(matcher: RequestMatcher, access: (Authentication, HttpServletRequest) => Boolean, channel: RequiredChannel.Value = RequiredChannel.Any) {
    addInterceptUrl(matcher, Arrays.asList(new ScalaWebConfigAttribute(access)), channel)
  }

  private[security] def addInterceptUrl(matcher: RequestMatcher, attributes: ju.List[ConfigAttribute], channel: RequiredChannel.Value) {
    require(!accessUrls.contains(matcher), "An identical RequestMatcher already exists: " + matcher)
    require(!accessUrls.exists(_._1.isInstanceOf[AnyRequestMatcher]), "A universal match has already been included in the " +
      "list, so any further interceptUrls will have no effect")
    accessUrls = accessUrls + (matcher -> attributes)
  }

  def accessDecisionVoters : List[AccessDecisionVoter[_]] = new ScalaWebVoter :: Nil

  lazy val accessDecisionManager : AccessDecisionManager = new AffirmativeBased(Arrays.asList(accessDecisionVoters: _*))
}

/**
 * Enum containing the options for secure channel
 */
object RequiredChannel extends Enumeration {
  val Http, Https, Any = Value
}
