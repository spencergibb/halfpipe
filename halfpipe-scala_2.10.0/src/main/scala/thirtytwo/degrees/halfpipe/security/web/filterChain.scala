package thirtytwo.degrees.halfpipe.security.web

import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter

import javax.servlet.Filter
import javax.servlet.http.HttpServletRequest

import java.util.Arrays
import java.lang.AssertionError
import org.springframework.security.web.context.{SecurityContextRepository, NullSecurityContextRepository, HttpSessionSecurityContextRepository, SecurityContextPersistenceFilter}
import org.springframework.security.web.savedrequest.{RequestCache, HttpSessionRequestCache, NullRequestCache, RequestCacheAwareFilter}
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.security.web.authentication._
import org.springframework.security.web.{SecurityFilterChain, AuthenticationEntryPoint}
import org.springframework.security.web.util.RequestMatcher

import FilterPositions._
import thirtytwo.degrees.halfpipe.security.Conversions

/**
 * This is the class that is most likely to be used as the starting point for a configuration.
 *
 * Extends `StatelessFilterChain` with the beans and filters which make up the configuration for a stateful application.
 */
abstract class FilterChain extends StatelessFilterChain with SessionManagement with AnonymousAuthentication {
  override val securityContextRepository: SecurityContextRepository = new HttpSessionSecurityContextRepository

  override val requestCache: RequestCache = new HttpSessionRequestCache

  lazy val requestCacheFilter = new RequestCacheAwareFilter(requestCache)

  protected[web] override def filtersInternal = (REQUEST_CACHE_FILTER, requestCacheFilter) :: super.filtersInternal
}

/**
 * Basic configuration class which provides the core security filters configured to handle stateless requests.
 *
 * @author Luke Taylor
 */
abstract class StatelessFilterChain extends Conversions with WebAccessControl with SecurityFilterChain {
  // Controls which requests will be handled by this filter chain
  val requestMatcher : RequestMatcher = "/**"

  // Services which are shared between filters
  val requestCache: RequestCache = new NullRequestCache
  val securityContextRepository: SecurityContextRepository = new NullSecurityContextRepository

  lazy val securityContextPersistenceFilter = new SecurityContextPersistenceFilter(securityContextRepository)

  val servletApiFilter = new SecurityContextHolderAwareRequestFilter()

  lazy val exceptionTranslationFilter = new ExceptionTranslationFilter(entryPoint, requestCache);

  lazy val entryPoint : AuthenticationEntryPoint = new Http403ForbiddenEntryPoint

  /**
   * Returns the filters provided by this class along with their index in the filter chain for sorting
   */
  protected[web] def filtersInternal: List[Tuple2[FilterPositions.Value,Filter]] = List(
      (SECURITY_CONTEXT_FILTER, securityContextPersistenceFilter),
      (SERVLET_API_SUPPORT_FILTER, servletApiFilter),
      (EXCEPTION_TRANSLATION_FILTER, exceptionTranslationFilter),
      (FILTER_SECURITY_INTERCEPTOR, filterSecurityInterceptor))

  def filters: List[Filter] =
    for {
      pair <- filtersInternal sortWith comparePositions
    } yield pair._2

  // Implementation of SecurityFilterChain for direct use as a Spring Security bean
  override final lazy val getFilters = Arrays.asList(filters:_*)

  override final def matches(request: HttpServletRequest) = requestMatcher.matches(request)
}

/**
 * Trait which assists with inserting custom filters before or after existing filters in the chain (experimental).
 */
trait InsertionHelper {
  def insertBefore(position: Filter, filter: Filter, target: List[Filter]): List[Filter] = insert(position == _, filter, target, true)
  def insertAfter(position: Filter, filter: Filter, target: List[Filter]): List[Filter] = insert(position == _, filter, target, false)

  def insertBefore(position: Class[_], filter: Filter, target: List[Filter]): List[Filter] = insert(position == _.getClass, filter, target, true)
  def insertAfter(position: Class[_], filter: Filter, target: List[Filter]): List[Filter] = insert(position == _.getClass, filter, target, false)

  private def insert(testPosition: Filter => Boolean, filter: Filter, target: List[Filter], before: Boolean): List[Filter] =
    target match {
      case f :: rest =>
        if (testPosition(f)) {
          if (before) {filter :: f :: rest} else {f :: filter :: rest}
        } else {
          f :: insert(testPosition, filter, rest, before)
        }
      case _ => throw new AssertionError("Failed to find filter satisfying required condition: " + testPosition)
    }
}

object InsertionHelper extends InsertionHelper
