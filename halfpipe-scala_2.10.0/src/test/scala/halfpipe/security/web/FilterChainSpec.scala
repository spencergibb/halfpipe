package halfpipe.security.web

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import halfpipe.security.Conversions._
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource
import org.scalatest.mock.MockitoSugar
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

import scala.collection.JavaConversions._
import WebAccessRules._
import halfpipe.security.AllowAllAuthenticationManager

trait AllowAllAuthentication extends FilterChainAuthenticationManager {
  override val authenticationManager = new AllowAllAuthenticationManager("ROLE_USER")
}

/**
 *
 * @author Luke Taylor
 */
class FilterChainSpec extends FlatSpec with ShouldMatchers with TestConversions with MockitoSugar  {
  "A FilterChain" should "allow the use of a custom RequestCache" in {
    val rc = mock[RequestCache]
    new FilterChain with AllowAllAuthentication {
      override val requestCache = rc
    }
  }
  it should "allow the use of a custom SessionAuthenticationStrategy" in {
    val sas = mock[SessionAuthenticationStrategy]
    new FilterChain with AllowAllAuthentication {
      override val sessionAuthenticationStrategy = sas
    }
  }
  it should "support adding of intercept URLs to the security interceptor" in {
    val chain = new FilterChain with AllowAllAuthentication {
      interceptUrl(matcher = "/AAA", access = permitAll)
      interceptUrl("/**", denyAll)
    }

    chain.filters

    val mds = chain.filterSecurityInterceptor.getSecurityMetadataSource match {
      case d : DefaultFilterInvocationSecurityMetadataSource => d
      case a : Any => fail("Expected DefaultFilterInvocationSecurityMetadataSource but was" + a)
    }

    mds.getAllConfigAttributes.size() should be (2)
    mds.getAttributes(stringToFilterInvocation("/AAA")).toList.head.asInstanceOf[ScalaWebConfigAttribute].predicate(null,null) should be(true)
    mds.getAttributes(stringToFilterInvocation("/XYZ")).toList.head.asInstanceOf[ScalaWebConfigAttribute].predicate(null,null) should be(false)
  }
  it should "not allow duplicate interceptUrl patterns" in {
    intercept[IllegalArgumentException] {
      new FilterChain with AllowAllAuthentication {
        interceptUrl(matcher = "/**", access = permitAll)
        interceptUrl("**", denyAll)
      }
    }

    intercept[IllegalArgumentException] {
      new FilterChain with AllowAllAuthentication {
        interceptUrl(matcher = "/aaa", access = permitAll)
        interceptUrl("/aaa", denyAll)
      }
    }
  }
  it should "not allow additional interceptUrls after a universal match" in {
    intercept[IllegalArgumentException] {
      new FilterChain with AllowAllAuthentication {
        interceptUrl("/aaa", permitAll)
        interceptUrl("/**", permitAll)
        interceptUrl("/bbb", permitAll)
      }
    }
  }
}
