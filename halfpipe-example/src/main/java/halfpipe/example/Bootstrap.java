package halfpipe.example;

import halfpipe.web.ServletContextBootstrap;
import halfpipe.web.ServletEnvironment;
import halfpipe.logging.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 10:25 AM
 */
@Component
public class Bootstrap implements ServletContextBootstrap {
    private static final Logger LOG = Log.forThisClass();

    int count = 0;

    @Override
    public void boostrap(ServletEnvironment handler) {
        //handler.addFilter("myfilter", new MyFilter(), new HashMap<String, String>(), "/*");
        FilterRegistration.Dynamic filter = handler.getServletContext().addFilter("myfilter", new MyFilter());
        filter.addMappingForUrlPatterns(null, true, "/*");
    }

    class MyFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            if (count++ < 5)
                LOG.info("Passing through {}", getClass().getSimpleName());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }
}
