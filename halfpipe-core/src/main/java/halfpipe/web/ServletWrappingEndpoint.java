package halfpipe.web;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: spencergibb
 * Date: 4/24/14
 * Time: 9:13 PM
 */
public abstract class ServletWrappingEndpoint extends AbstractServletWrappingController
        implements MvcEndpoint {

    protected String path;
    protected boolean sensitive;
    protected boolean enabled = true;

    protected ServletWrappingEndpoint(Class<?> servletClass, String servletName, String path,
                                      boolean sensitive, boolean enabled) {
        super(servletClass, servletName);
        this.path = path;
        this.sensitive = sensitive;
        this.enabled = enabled;
    }

    @RequestMapping("**")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.controller.handleRequest(request, response);
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
