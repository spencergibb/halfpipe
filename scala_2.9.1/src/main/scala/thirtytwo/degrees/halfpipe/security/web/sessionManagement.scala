package thirtytwo.degrees.halfpipe.security.web

import javax.servlet.Filter
import FilterPositions._
import org.springframework.security.web.session.{ConcurrentSessionFilter, SessionManagementFilter}
import org.springframework.security.web.authentication.session.{ConcurrentSessionControlStrategy, SessionFixationProtectionStrategy, NullAuthenticatedSessionStrategy, SessionAuthenticationStrategy}
import org.springframework.context.ApplicationListener
import org.springframework.security.core.session.{SessionRegistryImpl, SessionDestroyedEvent, SessionRegistry}

/**
 * Exposes the shared `SessionAuthenticationStrategy` reference.
 */
private[security] trait SessionAuthentication {
  val sessionAuthenticationStrategy: SessionAuthenticationStrategy = new NullAuthenticatedSessionStrategy
}

/**
 * Creates the default `SessionFixationProtectionStrategy` for use in a session-based app and adds
 * the `SessionManagementFilter`
 */
trait SessionManagement extends StatelessFilterChain with SessionAuthentication {
  override val sessionAuthenticationStrategy: SessionAuthenticationStrategy = new SessionFixationProtectionStrategy

  lazy val sessionManagementFilter: Filter = new SessionManagementFilter(securityContextRepository, sessionAuthenticationStrategy)

  protected[web] override def filtersInternal = (SESSION_MANAGEMENT_FILTER, sessionManagementFilter) :: super.filtersInternal
}

/**
 * Provides support for Spring Security's
 * [[http://static.springsource.org/spring-security/site/docs/3.1.x/reference/springsecurity-single.html#concurrent-sessions Concurrent Session Control]]
 * feature.
 */
trait ConcurrentSessionControl extends SessionManagement with ApplicationListener[SessionDestroyedEvent] {
  private var delegateEvents: Boolean = false

  /**
   * The `SessionRegistry` which keeps a record of the sessions used by a user.
   *
   * Defaults to a `SessionRegistryImpl`. Override to use a custom implementation.
   */
  lazy val sessionRegistry: SessionRegistry = {
    // SessionRegistry has not been overridden, so we need to delegate events to it
    delegateEvents = true
    new SessionRegistryImpl
  }

  /**
   * The URL which a usr will be redirected to if their session has been expired. Used to configure the
   * `ConcurrentSessionFilter`. Setting it is optional.
   */
  val sessionExpiredUrl: String = null

  /**
   * Overrides the default `SessionAuthenticationStrategy` with a `ConcurrentSessionControlStrategy` instance.
   */
  override val sessionAuthenticationStrategy = new ConcurrentSessionControlStrategy(sessionRegistry)

  /**
   * The filter which checks if a session has been "expired" by the `ConcurrentSessionControlStrategy`.
   */
  val concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry, sessionExpiredUrl)


  protected[web] override def filtersInternal = (CONCURRENT_SESSION_FILTER, concurrentSessionFilter) :: super.filtersInternal

  /**
   * Implementation of `ApplicationListener`. Delegates to the `SessionRegistry` unless it has been overridden with a
   * custom value.
   */
  def onApplicationEvent(event: SessionDestroyedEvent) {
    if (delegateEvents) {
      sessionRegistry.asInstanceOf[SessionRegistryImpl].onApplicationEvent(event)
    }
  }
}
