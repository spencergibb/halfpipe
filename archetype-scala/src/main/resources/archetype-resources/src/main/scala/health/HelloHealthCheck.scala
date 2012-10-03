#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.health

import org.springframework.stereotype.Service
import com.yammer.metrics.core.HealthCheck
import java.util.Date
import com.yammer.metrics.core.HealthCheck.Result

@Service
class HelloHealthCheck extends HealthCheck("hello") {
  def check() = {
    println(new Date()+ " works")
    Result.healthy("The Date class works")
  }
}
