package thirtytwo.degrees.halfpipe;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;
import static thirtytwo.degrees.halfpipe.HalfpipeConfiguration.*;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.yammer.metrics.web.DefaultWebappMetricsFilter;
import org.apache.catalina.servlets.DefaultServlet;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import thirtytwo.degrees.halfpipe.configuration.Configuration;
import thirtytwo.degrees.halfpipe.logging.Log;

import javax.servlet.*;
import java.util.Map;
import java.util.Set;

/**
 * http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/
 */
public class HalfpipeWebAppInitializer implements WebApplicationInitializer {
    private static final Log LOG = Log.forThisClass();

    static Object lock = new Object();
    static boolean initialized = false;

    public HalfpipeWebAppInitializer() {
    }

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        try {
            synchronized (lock) {
                if (initialized) return;

                initialized = true;

                AnnotationConfigWebApplicationContext rootCtx = Application.rootContext;

                // rather than sc.addListener(new ContextLoaderListener(rootCtx));
                // set the required servletcontext attribute to avoid loading beans twice
                sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootCtx);

                Configuration config = rootCtx.getBean(Configuration.class);

                // Filters
                addFilter(sc, "springSecurityFilterChain", new DelegatingFilterProxy(), ROOT_URL_PATTERN);
                addFilter(sc, "webappMetricsFilter", new DefaultWebappMetricsFilter(), ROOT_URL_PATTERN);

                // now the context for the Dispatcher servlet
                AnnotationConfigWebApplicationContext webCtx = createWebContext(Application.serverViewContextClass);
                webCtx.setParent(rootCtx); //TODO: does setParent need to be done?
                // The main Spring MVC servlet.
                String viewPattern = config.http.viewPattern.get();
                ServletRegistration.Dynamic viewServlet = addServlet(sc, "viewServlet", new DispatcherServlet(webCtx), 1,
                        viewPattern);

                // Jersey Servlet
                ServletRegistration.Dynamic jersey = addServlet(sc, "jersey-servlet", new SpringServlet(), 1,
                        config.http.resourcePattern.get());

                for (Map.Entry<String, Object> entry: jerseyProperties(config).entrySet()) {
                    jersey.setInitParameter(entry.getKey(), entry.getValue().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sc.log("Unable to initialize Halfpipe web application", e);
            throw new ServletException("Unable to initialize Halfpipe web application", e);
        }
    }

    private ServletRegistration.Dynamic addServlet(ServletContext servletContext, String servletName, Servlet servlet,
        int loadOnStartup, String... urlPatterns) {
            ServletRegistration.Dynamic reg = servletContext.addServlet(servletName, servlet);
        Assert.notNull(reg, "Unable to create servlet " + servletName);
        reg.setLoadOnStartup(loadOnStartup);
        Set<String> mappingConflicts = reg.addMapping(urlPatterns);

        if (!mappingConflicts.isEmpty()) {
            for (String s : mappingConflicts) {
                LOG.warn("Mapping conflict: {}", s);
            }
            throw new IllegalStateException(
                    "'"+servletName+"' cannot be mapped to '/' under Tomcat versions <= 7.0.14");
        }

        return reg;
    }

    private FilterRegistration.Dynamic addFilter(ServletContext servletContext, String filterName, Filter filter, String urlPattern) {
        FilterRegistration.Dynamic fr = servletContext.addFilter(filterName, filter);
        Assert.notNull(fr, "Unable to create filter "+filterName);
        //fr.setInitParameter("name", "value");
        fr.addMappingForUrlPatterns(null, true, urlPattern);
        return fr;
    }
}
