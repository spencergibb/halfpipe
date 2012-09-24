package thirtytwo.degrees.halfpipe.web;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.yammer.metrics.web.DefaultWebappMetricsFilter;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.Set;

/**
 * http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/
 */
public class HalfpipeWebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        try {
            // Create the root appcontext
            AnnotationConfigWebApplicationContext rootCtx = createContext("halfpipe.config.class");
            rootCtx.refresh();

            // Manage the lifecycle of the root appcontext
            sc.addListener(new ContextLoaderListener(rootCtx));

            // Filters
            addFilter(sc, "springSecurityFilterChain", new DelegatingFilterProxy(), "/*");
            addFilter(sc, "webappMetricsFilter", new DefaultWebappMetricsFilter(), "/*");

            // now the config for the Dispatcher servlet
            AnnotationConfigWebApplicationContext webCtx = createContext("halfpipe.view.config.class");

            // The main Spring MVC servlet.
            addServlet(sc, "viewServlet", new DispatcherServlet(webCtx), 1, getStringProp("halfpipe.view.url.pattern", "/*"));

            // Jersey Servlet
            ServletRegistration.Dynamic jersey = addServlet(sc, "jersey-servlet", new SpringServlet(), 1, getStringProp("halfpipe.url.pattern", "/ws/*"));
            jersey.setInitParameter("com.sun.jersey.config.property.packages", getStringProp("halfpipe.resource.packages").get());
            jersey.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            sc.log("Unable to initialize Halfpipe web application", e);
            throw new ServletException("Unable to initialize Halfpipe web application", e);
        }
    }

    private ServletRegistration.Dynamic addServlet(ServletContext servletContext, String servletName, Servlet servlet,
                                                   int loadOnStartup, DynamicStringProperty urlPattern) {
        ServletRegistration.Dynamic reg = servletContext.addServlet(servletName, servlet);
        Assert.notNull(reg, "Unable to create servlet "+servletName);
        reg.setLoadOnStartup(loadOnStartup);
        Set<String> mappingConflicts = reg.addMapping(urlPattern.get());

        if (!mappingConflicts.isEmpty()) {
            for (String s : mappingConflicts) {
                System.err.println("Mapping conflict: " + s);
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

    private AnnotationConfigWebApplicationContext createContext(String classConfigProperty) throws ClassNotFoundException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        DynamicStringProperty className = getStringProp(classConfigProperty);

        Class<?> appConfigClass = Class.forName(className.get());
        ctx.register(appConfigClass);
        return ctx;
    }

    private DynamicStringProperty getStringProp(String propName) {
        DynamicStringProperty prop = getStringProp(propName, null);

        Assert.hasText(prop.get(), propName + " must not be empty");
        return prop;
    }

    private DynamicStringProperty getStringProp(String propName, String defaultValue) {
        return DynamicPropertyFactory.getInstance().getStringProperty(propName, defaultValue);
    }
}
