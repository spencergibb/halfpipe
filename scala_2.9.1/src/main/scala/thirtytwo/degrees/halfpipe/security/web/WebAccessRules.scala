package thirtytwo.degrees.halfpipe.security.web

import org.springframework.security.core.Authentication
import javax.servlet.http.HttpServletRequest

import scala.collection.JavaConversions._
import org.springframework.security.web.util.IpAddressMatcher

/**
 * Access rule functions for use in `interceptUrl` access arguments.
 */
object WebAccessRules {

  def permitAll(a: Authentication, r: HttpServletRequest) = true

  def denyAll(a: Authentication, r: HttpServletRequest) = false

  def hasRole(role: String)(a: Authentication, r: HttpServletRequest) = a.getAuthorities.exists(role == _.getAuthority)

  def hasAnyRole(roles: String*)(a: Authentication, r: HttpServletRequest) = roles.exists(hasRole(_)(a, r))

  def hasAuthority(role: String)(a: Authentication, r: HttpServletRequest) = hasRole _

  def hasIpAddress(address: String)(a: Authentication, r: HttpServletRequest) = new IpAddressMatcher(address).matches(r)
}
