package thirtytwo.degrees.halfpipe.example.health;

import com.yammer.metrics.core.HealthCheck;
import org.springframework.stereotype.Service;
import thirtytwo.degrees.halfpipe.logging.Log;

import java.util.Date;

/**
 * User: spencergibb
 * Date: 9/21/12
 * Time: 6:25 PM
 */
@Service
public class HelloHealthCheck extends HealthCheck {
    private static final Log LOG = Log.forThisClass();

    public HelloHealthCheck() {
        super("hello");
    }

    @Override
    protected Result check() throws Exception {
        LOG.warn("new Date() {} works", new Date());
        return Result.healthy("The Date class works!");
    }
}
