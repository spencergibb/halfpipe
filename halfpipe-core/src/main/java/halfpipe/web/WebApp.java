package halfpipe.web;

import static halfpipe.Halfpipe.ROOT_URL_PATTERN;
import static halfpipe.HalfpipeConfiguration.jerseyProperties;
import static halfpipe.HalfpipeConfiguration.rootContext;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.yammer.metrics.web.DefaultWebappMetricsFilter;
import halfpipe.HalfpipeConfiguration;
import halfpipe.configuration.Configuration;
import halfpipe.context.DefaultViewContext;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 11:03 AM
 */
public class WebApp {

    @Inject
    Configuration config;

    @Autowired(required = false)
    WebBootstrap webBootstrap;

    public <WC> void configure(ServletContext servletContext, WC context, WebRegistrar<WC> registrar,
                                            boolean registerDefault) {
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
}
