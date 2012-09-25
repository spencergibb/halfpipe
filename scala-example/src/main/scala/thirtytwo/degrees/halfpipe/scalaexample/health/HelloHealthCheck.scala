package thirtytwo.degrees.halfpipe.scalaexample.health

import org.springframework.stereotype.Service
import com.yammer.metrics.core.HealthCheck
import java.util.Date
import com.yammer.metrics.core.HealthCheck.Result

/**
 * User: spencergibb
 * Date: 9/23/12
 * Time: 12:01 AM
 */
@Service
class HelloHealthCheck extends HealthCheck("hello") {
  def check() = {
    println(new Date()+ " works")
    Result.healthy("The Date class works")
  }
}
