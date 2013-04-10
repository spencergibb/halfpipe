package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import ch.qos.logback.classic.Level;
import com.netflix.config.*;
import halfpipe.context.MetricsContext;
import halfpipe.util.Duration;
import halfpipe.util.Size;

import javax.validation.constraints.Max;

/**
 * User: spencergibb
 * Date: 10/5/12
 * Time: 12:09 AM
 */
public class TestBadConfiguration {
    @Max(5)
    DynaProp<Integer> badProp = prop(10);
}
