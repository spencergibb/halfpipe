package thirtytwo.degrees.halfpipe.example.health;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User: gibbsb
 * Date: 9/21/12
 * Time: 6:25 PM
 */
@Service
public class HelloHealthCheck extends HealthCheck {

    public HelloHealthCheck() {
        super("hello");
    }

    @Override
    protected Result check() throws Exception {
        System.out.println(new Date() +" workes");
        return Result.healthy("The Date class works!");
    }
}
