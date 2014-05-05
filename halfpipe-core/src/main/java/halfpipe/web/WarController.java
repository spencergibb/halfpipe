package halfpipe.web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* User: spencergibb
* Date: 5/5/14
* Time: 4:56 PM
*/
public class WarController {

    private final EmbeddedWar embeddedWar;
    private final WebAppContext webapp;

    public WarController(EmbeddedWar embeddedWar, WebAppContext webapp) throws Exception {
        this.embeddedWar = embeddedWar;
        this.webapp = webapp;
        this.webapp.start();
    }

    //TODO: create handler mapping and war config object
    @RequestMapping("/**")
    public void handleWar(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException, ServletException {
        Request jettyRequest = findRequest(servletRequest);
        String requestURI = servletRequest.getRequestURI();
        requestURI = requestURI.substring("/hello".length());
        webapp.handle(requestURI, jettyRequest, servletRequest, servletResponse);
    }

    protected Request findRequest(Object o) {
        if (o instanceof Request) {
            return (Request) o;
        }
        if (o instanceof HttpServletRequestWrapper) {
            HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper) o;
            return findRequest(wrapper.getRequest());
        }
        return null;
    }

    public String getPath() {
        return embeddedWar.getPath();
    }
}
