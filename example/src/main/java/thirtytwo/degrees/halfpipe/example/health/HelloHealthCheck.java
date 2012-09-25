package thirtytwo.degrees.halfpipe.example.health;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User: spencergibb
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
        System.out.println(new Date() +" works");
        return Result.healthy("The Date class works!");
    }
}
