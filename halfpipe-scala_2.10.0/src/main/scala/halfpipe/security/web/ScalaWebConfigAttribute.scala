package halfpipe.security.web

import org.springframework.security.access.ConfigAttribute
import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication

/**
 * @author Luke Taylor
 */
case class ScalaWebConfigAttribute(predicate: (Authentication, HttpServletRequest) => Boolean) extends ConfigAttribute {
  def getAttribute = null
}
