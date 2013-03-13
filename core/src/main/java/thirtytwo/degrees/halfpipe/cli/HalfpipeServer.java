package thirtytwo.degrees.halfpipe.cli;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.yammer.metrics.web.DefaultWebappMetricsFilter;
import org.apache.commons.cli.CommandLine;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.*;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import thirtytwo.degrees.halfpipe.Application;
import thirtytwo.degrees.halfpipe.HalfpipeConfiguration;
import thirtytwo.degrees.halfpipe.configuration.Configuration;
import thirtytwo.degrees.halfpipe.logging.Log;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static thirtytwo.degrees.halfpipe.Halfpipe.ROOT_URL_PATTERN;
import static thirtytwo.degrees.halfpipe.HalfpipeConfiguration.*;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 11:44 PM
 */
public class HalfpipeServer implements CommandMarker {
    private static final Log LOG = Log.forThisClass();

    @Inject
    Configuration config;

    @CliAvailabilityIndicator({"server"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "server", help = "run halfpipe in tomcat http server")
    public String server(
            @CliOption(key = {"", "config"}, mandatory = true, help = "config file")
            String config ) throws Exception
    {
        run(null);
        //return "currently the server command only works as a command line argument";
        return null;
    }

    public void run(CommandLine commandLine) throws Exception {
        Server server = new Server(config.http.port.get());

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("."); //TODO: has to be set to non null?
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        //context.addServlet(JspServlet.class, "*.jsp");*/

        addFilter(context, "springSecurityFilterChain", new DelegatingFilterProxy(), ROOT_URL_PATTERN);
        addFilter(context, "webappMetricsFilter", new DefaultWebappMetricsFilter(), ROOT_URL_PATTERN);

        AnnotationConfigWebApplicationContext rootCtx = Application.rootContext;

        context.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootCtx);

        AnnotationConfigWebApplicationContext webContext = HalfpipeConfiguration.createWebContext(Application.serverViewContextClass);
        webContext.setParent(rootCtx);

        String viewPattern = config.http.viewPattern.get();
        addServlet(context, "viewServlet", new DispatcherServlet(webContext), viewPattern);
        addServlet(context, "default", new DefaultServlet(), "/favicon.ico");

        addServlet(context, "jersey-servlet", new SpringServlet(),
                config.http.resourcePattern.get(), jerseyProperties(config));
        /*Connector connector = new Connector(config.http.protocol.get());
        connector.setPort(config.http.port.get());
        connector.setURIEncoding(config.http.uriEncoding.get());*/

        //TODO https config
        //TODO use naming config
        //TODO ajp config
        //TODO serverXml config?

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);

        LOG.info("staring jetty on port {}", config.http.port.get());
        server.start();
        LOG.info("waiting for connections on port {}", config.http.port.get());
        server.join();
    }

    private ServletHolder addServlet(WebAppContext context, String name, Servlet servlet, String viewPattern) {
        return addServlet(context, name, servlet, viewPattern, new HashMap<String, String>());
    }
    private ServletHolder addServlet(WebAppContext context, String name, Servlet servlet, String viewPattern, Map<String, String> initParams) {
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setName(name);
        servletHolder.setInitParameters(initParams);
        context.addServlet(servletHolder, viewPattern);
        return servletHolder;
    }

    private FilterHolder addFilter(WebAppContext context, String name, Filter filter, String urlPattern) {
        FilterHolder filterHolder = new FilterHolder(filter);
        filterHolder.setName(name);
        context.addFilter(filterHolder, urlPattern, EnumSet.of(DispatcherType.REQUEST));
        return filterHolder;
    }

    private static void waitIndefinitely() {
        Object lock = new Object();

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException exception) {
                throw new Error("InterruptedException on wait Indefinitely lock:" + exception.getMessage(),
                        exception);
            }
        }
    }

}
