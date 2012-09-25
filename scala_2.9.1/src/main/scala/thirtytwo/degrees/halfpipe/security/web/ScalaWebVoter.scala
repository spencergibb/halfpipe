package thirtytwo.degrees.halfpipe.security.web

import org.springframework.security.web.FilterInvocation
import org.springframework.security.access.{ConfigAttribute, AccessDecisionVoter}
import org.springframework.security.core.Authentication
import java.{util => ju}

import scala.collection.JavaConversions._

/**
 * `AccessDecisionVoter` which checks for a `ScalaWebConfigAttribute`and uses it to decide whether to grant access.
 * Abstains if no attribute is found. Otherwise it applies the attribute's `predicate` function to the
 * current `Authentication` and `HttpServletRequest`, voting to grant access if the function returns `true` and
 * otherwise denying access.
 */
private[web] class ScalaWebVoter extends AccessDecisionVoter[FilterInvocation] {
  def vote(authentication: Authentication, secured : FilterInvocation, attributes: ju.Collection[ConfigAttribute]) = {
    attributes.find(_.isInstanceOf[ScalaWebConfigAttribute]) match {
      case Some(s) =>
        if (s.asInstanceOf[ScalaWebConfigAttribute].predicate.apply(authentication, secured.getHttpRequest))
          AccessDecisionVoter.ACCESS_GRANTED
        else
          AccessDecisionVoter.ACCESS_DENIED
      case None => AccessDecisionVoter.ACCESS_ABSTAIN
    }
  }

  def supports(clazz: Class[_]) = clazz.isAssignableFrom(classOf[FilterInvocation])

  def supports(attribute: ConfigAttribute) = attribute.isInstanceOf[ScalaWebConfigAttribute]
}
