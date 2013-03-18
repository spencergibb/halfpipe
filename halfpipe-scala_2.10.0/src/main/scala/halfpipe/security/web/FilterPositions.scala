package halfpipe.security.web

import javax.servlet.Filter

/**
 * Enum of standard filter positions.
 *
 * Allows traits/classes which contribute filters by overriding the `filtersInternal` method to specify the
 * position at which the filter should occur.
 *
 * The return value of `filtersInternal` are sorted based on these values to order the filter chain correctly.
 */
private[web] object FilterPositions extends Enumeration {
  val CHANNEL_FILTER,
    CONCURRENT_SESSION_FILTER,
    SECURITY_CONTEXT_FILTER,
    LOGOUT_FILTER,
    X509_FILTER,
    PRE_AUTH_FILTER,
    CAS_FILTER,
    FORM_LOGIN_FILTER,
    OPENID_FILTER,
    LOGIN_PAGE_FILTER,
    DIGEST_AUTH_FILTER,
    BASIC_AUTH_FILTER,
    REQUEST_CACHE_FILTER,
    SERVLET_API_SUPPORT_FILTER,
    JAAS_API_SUPPORT_FILTER,
    REMEMBER_ME_FILTER,
    ANONYMOUS_FILTER,
    SESSION_MANAGEMENT_FILTER,
    EXCEPTION_TRANSLATION_FILTER,
    FILTER_SECURITY_INTERCEPTOR,
    SWITCH_USER_FILTER = Value

  /**
   * The method used when sorting the list returned by `filtersInternal`.
   */
  private[web] def comparePositions (a: Tuple2[FilterPositions.Value,Filter], b: Tuple2[FilterPositions.Value,Filter]) = {
    a._1 < b._1
  }

}
