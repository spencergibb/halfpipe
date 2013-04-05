package halfpipe.web;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.EnumSet;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 10:58 AM
 */
//TODO: spring profiles and inject WebRegistrar
public class JettyWebRegistrar implements WebRegistrar<WebAppContext> {

    WebAppContext context;

    public void setContext(WebAppContext context) {
        this.context = context;
    }

    public ServletHolder addServlet(String name, Servlet servlet, String viewPattern, Map<String, String> initParams) {
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setName(name);
        servletHolder.setInitParameters(initParams);
        context.addServlet(servletHolder, viewPattern);
        return servletHolder;
    }

    public FilterHolder addFilter(String name, Filter filter, String urlPattern, Map<String, String> initParams) {
        FilterHolder filterHolder = new FilterHolder(filter);
        filterHolder.setName(name);
        filterHolder.setInitParameters(initParams);
        context.addFilter(filterHolder, urlPattern, EnumSet.of(DispatcherType.REQUEST));
        return filterHolder;
    }
}
