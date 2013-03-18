package halfpipe.security.web

import collection.immutable.ListMap
import java.{util => ju}

/**
 * Internal conversions
 */
private[web] object WebConversions {
  implicit def listMapAsJavaLinkedHashMap[A, B](m : ListMap[A, B]): ju.LinkedHashMap[A, B] = {
    val result = new ju.LinkedHashMap[A,B]
    m foreach { case (key, value) => result.put(key, value)}
    result
  }
}
