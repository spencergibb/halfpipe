package thirtytwo.degrees.halfpipe.security.web

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter

import InsertionHelper._

/**
 * @author Luke Taylor
 */
class StatelessFilterChainSpec extends FlatSpec with ShouldMatchers with TestConversions with MockitoSugar {

  "a StatelessFilterChain" should "allow easy insertion of additional filters" in {
    val chain = new StatelessFilterChain with AllowAllAuthentication with InsertionHelper {
      override def filters = {
        insertAfter(classOf[ExceptionTranslationFilter], new X509AuthenticationFilter, super.filters)
      }
    }

    assert(chain.filters(3).isInstanceOf[X509AuthenticationFilter])

    val chain2 = new StatelessFilterChain with AllowAllAuthentication {
      override def filters = {
        insertBefore(classOf[ExceptionTranslationFilter], new X509AuthenticationFilter, super.filters)
      }
    }

    assert(chain2.filters(2).isInstanceOf[X509AuthenticationFilter])

    val chain3 = new StatelessFilterChain with AllowAllAuthentication {
      override def filters = {
        insertBefore(exceptionTranslationFilter, new X509AuthenticationFilter, super.filters)
      }
    }

    assert(chain3.getFilters.get(2).isInstanceOf[X509AuthenticationFilter])
  }

}
