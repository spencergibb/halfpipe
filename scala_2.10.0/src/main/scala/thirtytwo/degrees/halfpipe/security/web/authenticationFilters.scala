package thirtytwo.degrees.halfpipe.security.web

import org.springframework.security.authentication._
import org.springframework.security.web.authentication.logout._
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
import org.springframework.security.openid.{OpenIDAuthenticationProvider, OpenID4JavaConsumer, OpenIDAuthenticationFilter}
import org.springframework.security.web.authentication.www.{BasicAuthenticationFilter, BasicAuthenticationEntryPoint}
import org.springframework.security.web.authentication._
import org.springframework.security.web.authentication.rememberme._
import java.util.Arrays

import FilterPositions._
import thirtytwo.degrees.halfpipe.security._

/**
 * Encapsulates the internal `ProviderManager` and `AuthenticationProvider`s user by the
 * filter authentication mechanisms, as well as the reference to the parent
 * `AuthenticationManager` which the user must inject into their configuration.
 */
private[security] trait FilterChainAuthenticationManager {
  /**
   * The reference which must be set to the `AuthenticationManager` which will be used to authenticate users.
   */
  val authenticationManager : AuthenticationManager

  protected[web] def authenticationProviders : List[AuthenticationProvider] = Nil

  private[security] lazy val internalAuthenticationManager : ProviderManager =
    new ProviderManager(Arrays.asList(authenticationProviders:_*), authenticationManager)
}

/**
 * Provides support for Spring Security's
 * [[http://static.springsource.org/spring-security/site/docs/3.1.x/reference/springsecurity-single.html#anonymous Anonymous Authentication]].
 *
 * This trait is already added to [[security.FilterChain]] class, so you don't need to add it explicitly
 * if you are using this class.
 */
trait AnonymousAuthentication extends StatelessFilterChain with FilterChainAuthenticationManager {
  val anonymousKey = "replaceMeWithAProperKey"
  val anonymousProvider = new AnonymousAuthenticationProvider(anonymousKey)

  lazy val anonymousFilter = new AnonymousAuthenticationFilter(anonymousKey)

  protected[web] override def authenticationProviders = anonymousProvider :: super.authenticationProviders
  protected[web] override def filtersInternal = (ANONYMOUS_FILTER, anonymousFilter) :: super.filtersInternal
}

/**
 * Adds a `LogoutFilter` to the filter stack.
 *
 * The filter responds to the URL ''//j_spring_security_logout'' by default, but this can easily be modified:
 *
 * {{{
 *   logoutFilter.setFilterProcessesUrl("/my_custom_logout_url")
 * }}}
 */
trait Logout extends StatelessFilterChain with RememberMeServicesAware {
  /**
   * Default `LogoutHandler`s used to configure the logout filter.
   */
  lazy val logoutHandlers: List[LogoutHandler] = {
    val sclh = new SecurityContextLogoutHandler()
    rememberMeServices match {
      case l: LogoutHandler =>  sclh :: l :: Nil
      case _ => sclh :: Nil
    }
  }

  /**
   * `LogoutSuccessHandler` which is injected into the logout filter. Override to customize the logout
   * destination.
   */
  val logoutSuccessHandler : LogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler()

  /**
   * The `LogoutFilter` instance.
   */
  lazy val logoutFilter = new LogoutFilter(logoutSuccessHandler, logoutHandlers : _*)

  protected[web] override def filtersInternal = (LOGOUT_FILTER, logoutFilter) :: super.filtersInternal
}

/**
 * Provides the `loginPage` URL and overrides the `AuthenticationEntryPoint` to redirect to the page
 * when a user needs to be authenticated.
 */
private[web] sealed trait LoginPage extends StatelessFilterChain {
  /**
   * The login page URL.
   *
   * When using FormLogin or OpenID, this should be set to the location of the login page. Alternatively,
   * the `LoginPageGenerator` trait can be mixed in to automatically create a username/password login form.
   */
  val loginPage: String

  override lazy val entryPoint : AuthenticationEntryPoint = {
    new LoginUrlAuthenticationEntryPoint(loginPage)
  }
}

/**
 * Automatically generates a login page for form-login.
 */
trait LoginPageGenerator extends StatelessFilterChain with FormLogin {
  final override lazy val loginPage = "/spring_security_login"

  private lazy val loginPageFilter = new DefaultLoginPageGeneratingFilter(formLoginFilter.asInstanceOf[UsernamePasswordAuthenticationFilter])

  protected[web] override def filtersInternal = (LOGIN_PAGE_FILTER, loginPageFilter) :: super.filtersInternal
}

/**
 * Supports username/password authentication using a form (login page).
 *
 * Adds a `UsernamePasswordAuthenticationFilter`.
 *
 */
trait FormLogin extends StatelessFilterChain with EventPublisher
    with LoginPage
    with RememberMeServicesAware
    with SessionAuthentication
    with FilterChainAuthenticationManager {

  /**
   * The filter instance. Use this to customize the filter behaviour (for example using a custom
   * `AuthenticationSuccessHandler` or `AuthenticationFailureHandler`).
   */
  lazy val formLoginFilter = {
    val filter = new UsernamePasswordAuthenticationFilter
    filter.setAuthenticationManager(authenticationManager)
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy)
    filter.setRememberMeServices(rememberMeServices)
    filter.setApplicationEventPublisher(eventPublisher)
    filter
  }

  protected[web] override def filtersInternal = (FORM_LOGIN_FILTER, formLoginFilter) :: super.filtersInternal
}

/**
 * Support for OpenID authentication.
 * Requires that you set the `loginPage` and `userDetailsService` values.
 */
trait OpenID extends StatelessFilterChain with EventPublisher
    with LoginPage
    with SessionAuthentication
    with RememberMeServicesAware
    with UserService
    with FilterChainAuthenticationManager {

  lazy val openIDFilter = {
    val filter = new OpenIDAuthenticationFilter
    filter.setConsumer(new OpenID4JavaConsumer)
    filter.setRememberMeServices(rememberMeServices)
    filter.setAuthenticationManager(internalAuthenticationManager)
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy)
    filter.setApplicationEventPublisher(eventPublisher)
    filter
  }
  lazy val openIDProvider = {
    val provider = new OpenIDAuthenticationProvider
    provider.setUserDetailsService(userDetailsService)
    provider
  }

  protected[web] override def authenticationProviders = openIDProvider :: super.authenticationProviders
  protected[web] override def filtersInternal = (OPENID_FILTER, openIDFilter) :: super.filtersInternal
}

/**
 * Support for Basic authentication. Adds a `BasicAuthenticationFilter`
 */
trait BasicAuthentication extends StatelessFilterChain with FilterChainAuthenticationManager {
  val basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint()

  lazy val basicAuthenticationFilter =
    new BasicAuthenticationFilter(authenticationManager, basicAuthenticationEntryPoint)

  override lazy val entryPoint : AuthenticationEntryPoint = basicAuthenticationEntryPoint

  protected[web] override def filtersInternal = (BASIC_AUTH_FILTER, basicAuthenticationFilter) :: super.filtersInternal
}

private[web] sealed trait RememberMeServicesAware {
  lazy val rememberMeServices: RememberMeServices = new NullRememberMeServices
}

/**
 * Adds remember-me support using a `TokenBasedRememberMeServices`
 */
trait RememberMe extends StatelessFilterChain
    with RememberMeServicesAware
    with EventPublisher
    with UserService
    with FilterChainAuthenticationManager {

  val rememberMeKey = "todo"

  lazy val rememberMeFilter = {
    val filter = new RememberMeAuthenticationFilter(internalAuthenticationManager, rememberMeServices)
    filter.setApplicationEventPublisher(eventPublisher)
    filter
  }

  val rememberMeProvider = new RememberMeAuthenticationProvider(rememberMeKey)

  override lazy val rememberMeServices: RememberMeServices = new TokenBasedRememberMeServices(rememberMeKey, userDetailsService)

  protected[web] override def authenticationProviders = rememberMeProvider :: super.authenticationProviders
  protected[web] override def filtersInternal = (REMEMBER_ME_FILTER, rememberMeFilter) :: super.filtersInternal
}

/**
 * Persistent token-based remember-me.
 */
trait PersistentRememberMe extends RememberMe {
  /**
   * The token repository where the remember-me tokens are stored.
   *
   * This would usually be overridden with a JDBC implementation.
   */
  lazy val tokenRepository : PersistentTokenRepository = new InMemoryTokenRepositoryImpl

  override lazy val rememberMeServices =
    new PersistentTokenBasedRememberMeServices(rememberMeKey, userDetailsService, tokenRepository)
}
