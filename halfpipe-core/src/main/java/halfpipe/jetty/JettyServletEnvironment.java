package halfpipe.jetty;

import halfpipe.web.ServletEnvironment;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.util.EnumSet;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 10:58 AM
 */
//TODO: spring profiles and inject ServletEnvironment
public class JettyServletEnvironment implements ServletEnvironment {

    WebAppContext context;

    public JettyServletEnvironment() {
        context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("."); //TODO: has to be set to non null?
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    public WebAppContext getContext() {
        return context;
    }

    public ServletContext getServletContext() {
        return context.getServletContext();
    }

    public ServletHolder addServlet(String name, Servlet servlet, Map<String, String> initParams, String... viewPatterns) {
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setName(name);
        servletHolder.setInitParameters(initParams);
        //TODO: multiple url patterns
        context.addServlet(servletHolder, viewPatterns[0]);
        return servletHolder;
    }

    public FilterHolder addFilter(String name, Filter filter, Map<String, String> initParams, String... urlPatterns) {
        FilterHolder filterHolder = new FilterHolder(filter);
        filterHolder.setName(name);
        filterHolder.setInitParameters(initParams);
        context.addFilter(filterHolder, urlPatterns[0], EnumSet.of(DispatcherType.REQUEST));
        return filterHolder;
    }

    @Override
    public boolean isRegisterDefault() {
        return true;
    }
}
