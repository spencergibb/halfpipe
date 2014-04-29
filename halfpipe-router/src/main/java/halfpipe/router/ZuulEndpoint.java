package halfpipe.router;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import halfpipe.web.ServletWrappingEndpoint;
import org.springframework.stereotype.Component;

/**
 * User: spencergibb
 * Date: 4/24/14
 * Time: 9:12 PM
 */
@Component
public class ZuulEndpoint extends ServletWrappingEndpoint {

    public ZuulEndpoint() {
        super(HystrixMetricsStreamServlet.class, "/zuul", "hystrixStream", false, true);
    }
}
