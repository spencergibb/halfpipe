package halfpipe.security

import org.springframework.security.web.util.{AntPathRequestMatcher, AnyRequestMatcher}
import java.{util => ju}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Useful Spring Security implicit conversions.
 */
trait Conversions {
  implicit def stringToRequestMatcher(pattern: String) = pattern match {
    case "**" | "/**" => new AnyRequestMatcher
    case _  => new AntPathRequestMatcher(pattern)
  }

  implicit def stringToGrantedAuthority(authority: String): GrantedAuthority = {
    new SimpleGrantedAuthority(authority)
  }

  implicit def stringToGrantedAuthorityList(authority: String) : ju.List[GrantedAuthority] = {
    ju.Arrays.asList(new SimpleGrantedAuthority(authority))
  }

  implicit def stringListToGrantedAuthorityList(authorities: List[String]) : ju.List[GrantedAuthority] = {
    val auths = for { auth <- authorities } yield { new SimpleGrantedAuthority(auth) }
    ju.Arrays.asList(auths: _*)
  }
}

object Conversions extends Conversions
