package halfpipe.security.web

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter
import org.springframework.security.core.userdetails.{User, UserDetailsService}

class RememberMeSpec extends FlatSpec with ShouldMatchers with TestConversions {
  val filterChainWithFormRememberMe = new FilterChain with FormLogin with Logout with RememberMe with LoginPageGenerator with AllowAllAuthentication {
    override val userDetailsService = new UserDetailsService {
      def loadUserByUsername(username: String) = new User(username, username, "ROLE_USER")
    }
  }
  "A FilterChain with RememberMe" should "have a RememberMeAuthenticationFilter" in {
    assert(filterChainWithFormRememberMe.filters.exists(_.isInstanceOf[RememberMeAuthenticationFilter]))
  }

}
