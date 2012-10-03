#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.health;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HelloHealthCheck extends HealthCheck {

    public HelloHealthCheck() {
        super("hello");
    }

    @Override
    protected Result check() throws Exception {
        System.out.println(new Date() +" works");
        return Result.healthy("The Date class works!");
    }
}
