package thirtytwo.degrees.halfpipe

import thirtytwo.degrees.halfpipe.security.web._

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.util.IpAddressMatcher
import org.springframework.security.core.userdetails.{UsernameNotFoundException, User, UserDetailsService}
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import java.util.Arrays
import org.springframework.security.core.Authentication

import org.springframework.security.authentication.{DefaultAuthenticationEventPublisher, ProviderManager}

import thirtytwo.degrees.halfpipe.security.web.WebAccessRules._
import org.springframework.security.access.event.{LoggerListener => AccessLoggerListener}
import org.springframework.security.authentication.event.{LoggerListener => AuthLoggerListener}

/**
 * An @Configuration with sample filter chains defined as separate beans.
 *
 * @author Luke Taylor
 */
@Configuration
class SecurityConfig {

  /**
   * The FilterChainProxy bean which is delegated to from web.xml
   */
  @Bean
  def springSecurityFilterChain = new FilterChainProxy(basicAuthFilterChain)

  /**
   * A Basic authentication configuration
   */
  @Bean
  def basicAuthFilterChain: FilterChain =
    new FilterChain with BasicAuthentication {
      val authenticationManager = testAuthenticationManager
      interceptUrl("/admin/**", hasRole("ROLE_ADMIN"))
      interceptUrl("/ws/**", hasRole("ROLE_USER"))
      interceptUrl("/", permitAll)
      basicAuthenticationEntryPoint.setRealmName("scalaexample")
    }

  /**
   * Simple AuthenticationManager setup for testing.
   */
  @Bean
  def testAuthenticationManager = {
    val provider = new DaoAuthenticationProvider
    provider.setUserDetailsService(testUserDetailsService)
    val am = new ProviderManager(Arrays.asList(provider))
    am.setAuthenticationEventPublisher(authenticationEventPublisher)
    am
  }

  @Bean
  def authenticationEventPublisher = new DefaultAuthenticationEventPublisher

  /**
   * Test UserDetailsService which accepts any username and returns a user object which has a password equal to the
   * username and which is assigned the single authority "ROLE_USER".
   */
  @Bean
  def testUserDetailsService = {
    import thirtytwo.degrees.halfpipe.security.Conversions._

    new UserDetailsService {
      def loadUserByUsername(username: String) = username match {
        case "admin" => new User(username, "password", List("ROLE_USER", "ROLE_ADMIN"))
        case "user" => new User(username, "password", "ROLE_USER")
        case _ => throw new UsernameNotFoundException(username)
      }
    }
  }

  /*@Bean def accessLogger = new AccessLoggerListener

  @Bean def authenticationLogger = new AuthLoggerListener*/

  /**
   * A form-login configuration with remember-me and other standard options.
   * Namespace equivalent would be:
   * <pre>
   *   &lt;http use-expressions="true">
   *     &lt;intercept-url pattern="/" access="permitAll" />
   *     &lt;intercept-url pattern="/&#42*" access="hasRole('ROLE_USER')" />
   *     &lt;form-login />
   *     &lt;logout />
   *     &lt;remember-me />
   *   &lt;/http>
   * </pre>
   */
  //@Bean
  //  def simpleFormLoginChain =
  //    new FilterChain with FormLogin with Logout with RememberMe with LoginPageGenerator {
  //      val authenticationManager = testAuthenticationManager
  //      val userDetailsService = testUserDetailsService
  //      interceptUrl("/", permitAll)
  //      interceptUrl("/**", hasRole("ROLE_USER"))
  //    }

//  @Bean
//  def filterChainWithLotsOfStuff = {
//    new FilterChain with BasicAuthentication with FormLogin with Logout with RememberMe with ConcurrentSessionControl with LoginPageGenerator {
//      interceptUrl("/", permitAll)
//      interceptUrl("/scala*", permitAll)
//      //interceptUrl("/onlylocal*", isLocalhost)
//      //interceptUrl("/eventime*", allowOnEvenTime)
//      interceptUrl("/**", hasRole("ROLE_USER"))
//      sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(true)
//      val authenticationManager = testAuthenticationManager
//      val userDetailsService = testUserDetailsService
//    }
//  }

  // Some access rules
 /* def allowOnEvenTime(a: Authentication, r: HttpServletRequest) = java.lang.System.currentTimeMillis() % 2 == 0

  val localhostMatcher = new IpAddressMatcher("127.0.0.1")

  def isLocalhost(a: Authentication, r: HttpServletRequest) = localhostMatcher.matches(r)*/
}
