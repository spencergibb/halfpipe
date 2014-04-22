package halfpipe.mvc;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: spencergibb
 * Date: 4/22/14
 * Time: 3:16 PM
 */
public class HystrixStreamEndpoint implements MvcEndpoint, InitializingBean,
        ApplicationContextAware, ServletContextAware {

    private String path;

    private boolean sensitive;

    private boolean enabled = true;

    private final ServletWrappingController controller = new ServletWrappingController();

    public HystrixStreamEndpoint() {
        this.path = "/hystrix.stream";
        this.controller.setServletClass(HystrixMetricsStreamServlet.class);
        this.controller.setServletName("hystrixStream");
    }

    @RequestMapping("/**")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.controller.handleRequest(request, response);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.controller.afterPropertiesSet();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.controller.setServletContext(servletContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.controller.setApplicationContext(applicationContext);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isSensitive() {
        return sensitive;
    }

    @Override
    public Class<? extends Endpoint> getEndpointType() {
        return null;
    }
}
