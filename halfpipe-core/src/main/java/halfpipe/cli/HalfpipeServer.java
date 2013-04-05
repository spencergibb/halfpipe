package halfpipe.cli;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.yammer.metrics.web.DefaultWebappMetricsFilter;
import halfpipe.HalfpipeConfiguration;
import halfpipe.WebBootstrap;
import halfpipe.WebRegistrar;
import halfpipe.configuration.Configuration;
import halfpipe.context.DefaultViewContext;
import halfpipe.logging.Log;
import org.apache.commons.cli.CommandLine;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static halfpipe.Halfpipe.ROOT_URL_PATTERN;
import static halfpipe.HalfpipeConfiguration.jerseyProperties;
import static halfpipe.HalfpipeConfiguration.rootContext;

/**
 * User: spencergibb
 * Date: 9/26/12
 * Time: 11:44 PM
 */
public class HalfpipeServer implements CommandMarker, WebRegistrar<WebAppContext> {
    private static final Log LOG = Log.forThisClass();

    @Inject
    Configuration config;

    @Autowired(required = false)
    WebBootstrap webBootstrap;

    WebAppContext context;

    @CliAvailabilityIndicator({"server"})
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "server", help = "run halfpipe in jetty http server")
    public String server(
            @CliOption(key = {"", "config"}, mandatory = true, help = "config file")
            String config ) throws Exception
    {
        run(null);
        return null;
    }

    public void run(CommandLine commandLine) throws Exception {
        Server server = new Server(config.http.port.get());

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("."); //TODO: has to be set to non null?
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        //context.addServlet(JspServlet.class, "*.jsp");*/

        configureWebApp(context.getServletContext(), context, this, config, webBootstrap, true);

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

    public static <WC> void configureWebApp(ServletContext servletContext, WC context, WebRegistrar<WC> registrar,
                                            Configuration config, WebBootstrap webBootstrap, boolean registerDefault) {
        HashMap<String, String> emptyInitParams = new HashMap<String, String>();

        // rather than sc.addListener(new ContextLoaderListener(rootCtx));
        // set the required servletcontext attribute to avoid loading beans twice
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);
        registrar.setContext(context);

        registrar.addFilter("springSecurityFilterChain", new DelegatingFilterProxy(), ROOT_URL_PATTERN, emptyInitParams);
        registrar.addFilter("webappMetricsFilter", new DefaultWebappMetricsFilter(), ROOT_URL_PATTERN, emptyInitParams);

        ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory) rootContext.getAutowireCapableBeanFactory();

        Class<?> viewContextClass;
        if (beanFactory.containsBean("viewContextClass")) {
            viewContextClass = beanFactory.getBean("viewContextClass", Class.class);
        } else {
            //default view context
            viewContextClass = DefaultViewContext.class;
            beanFactory.registerSingleton("springSecurityFilterChain", new FilterChainProxy());
        }
        // now the context for the Dispatcher servlet
        AnnotationConfigWebApplicationContext webContext = HalfpipeConfiguration.createWebContext(viewContextClass);
        webContext.setParent(rootContext);

        // The main Spring MVC servlet.
        String viewPattern = config.http.viewPattern.get();
        registrar.addServlet("viewServlet", new DispatcherServlet(webContext), viewPattern, emptyInitParams);

        if (registerDefault)
            registrar.addServlet("default", new DefaultServlet(), "/favicon.ico", emptyInitParams);

        Map<String, Object> resources = rootContext.getBeansWithAnnotation(Path.class);

        registrar.addServlet("jersey-servlet", new SpringServlet(),
                config.http.resourcePattern.get(), jerseyProperties(resources, config));

        //custom webapp initialization
        if (webBootstrap != null) {
            webBootstrap.boostrap(registrar);
        }
    }

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
