package halfpipe.example;

import halfpipe.WebBootstrap;
import halfpipe.WebRegistrar;
import halfpipe.logging.Log;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 10:25 AM
 */
@Component
public class Bootstrap implements WebBootstrap {
    private static final Log LOG = Log.forThisClass();

    @Override
    public void boostrap(WebRegistrar<?> registrar) {
        registrar.addFilter("myfilter", new MyFilter(), "/*", new HashMap<String, String>());
    }

    class MyFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            LOG.info("Passing through {}", getClass().getSimpleName());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }
}
