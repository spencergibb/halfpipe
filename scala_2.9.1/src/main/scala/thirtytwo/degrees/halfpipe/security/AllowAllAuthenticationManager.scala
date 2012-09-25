package thirtytwo.degrees.halfpipe.security

import org.springframework.security.authentication.{UsernamePasswordAuthenticationToken, BadCredentialsException, AuthenticationManager}
import org.springframework.security.core.Authentication

import Conversions._

/**
 * `AuthenticationManager` for testing.
 *
 * Accepts any authentication request
 */
class AllowAllAuthenticationManager (authorities : String*) extends AuthenticationManager {

  def authenticate(authentication: Authentication) = authentication match {
    case u : UsernamePasswordAuthenticationToken =>
      if (u.getPrincipal == u.getCredentials)
        new UsernamePasswordAuthenticationToken(u.getName, u.getName, authorities.toList)
      else
        badCredentials()
    case _ => badCredentials()
  }

  def badCredentials() = {
    throw new BadCredentialsException("Bad Credentials")
  }
}
