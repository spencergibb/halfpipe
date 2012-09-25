package thirtytwo.degrees.halfpipe.web;

import static thirtytwo.degrees.halfpipe.Halfpipe.*;

import com.google.common.collect.Maps;
import com.netflix.config.*;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.yammer.metrics.web.DefaultWebappMetricsFilter;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import thirtytwo.degrees.halfpipe.jersey.HalfpipeResourceConfig;

import javax.servlet.*;
import java.util.Map;
import java.util.Set;

/**
 * http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/
 */
public class HalfpipeWebAppInitializer implements WebApplicationInitializer {

    static {
        System.setProperty("archaius.configurationSource.defaultFileName", HALFPIPE_PROPERTIES_FILENAME);
    }

    public HalfpipeWebAppInitializer() {
    }

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        try {
            createConfig(sc);

            // Create the root appcontext
            AnnotationConfigWebApplicationContext rootCtx = createContext(PROP_CONFIG_CLASS);
            rootCtx.refresh();

            // Manage the lifecycle of the root appcontext
            sc.addListener(new ContextLoaderListener(rootCtx));

            // Filters
            addFilter(sc, "springSecurityFilterChain", new DelegatingFilterProxy(), ROOT_URL_PATTERN);
            addFilter(sc, "webappMetricsFilter", new DefaultWebappMetricsFilter(), ROOT_URL_PATTERN);

            // now the config for the Dispatcher servlet
            AnnotationConfigWebApplicationContext webCtx = createContext(PROP_VIEW_CONFIG_CLASS);

            // The main Spring MVC servlet.
            addServlet(sc, "viewServlet", new DispatcherServlet(webCtx), 1, getStringProp(PROP_VIEW_URL_PATTERN, ROOT_URL_PATTERN));

            if (DynamicPropertyFactory.getInstance().getBooleanProperty(PROP_INSTALL_DEFAULT_SERVLET, false).get()) {
                addServlet(sc, HALFPIPE_DEFAULT_SERVLET, new DefaultServlet(), 1, ROOT_URL_PATTERN);
            }

            // Jersey Servlet
            ServletRegistration.Dynamic jersey = addServlet(sc, "jersey-servlet", new SpringServlet(), 1,
                    getStringProp(HALFPIPE_URL_PATTERN, RESOURCE_URL_PATTERN));
            jersey.setInitParameter(ServletContainer.RESOURCE_CONFIG_CLASS, HalfpipeResourceConfig.class.getName());
            jersey.setInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES, getStringProp(HALFPIPE_RESOURCE_PACKAGES).get());
            jersey.setInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            sc.log("Unable to initialize Halfpipe web application", e);
            throw new ServletException("Unable to initialize Halfpipe web application", e);
        }
    }

    private void createConfig(ServletContext sc) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(PROP_INSTALL_DEFAULT_SERVLET, (sc.getNamedDispatcher("default") == null));
        System.err.println("Config: "+map);

        ConcurrentCompositeConfiguration configuration = new ConcurrentCompositeConfiguration();
        configuration.addConfiguration(new ConcurrentMapConfiguration(map));
        configuration.addConfiguration(new SystemConfiguration(), DynamicPropertyFactory.SYS_CONFIG_NAME);
        //configuration.addConfiguration(new ConcurrentMapConfiguration(ClasspathPropertiesConfiguration.));
        try {
            DynamicURLConfiguration defaultURLConfig = new DynamicURLConfiguration();
            configuration.addConfiguration(defaultURLConfig, DynamicPropertyFactory.URL_CONFIG_NAME);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        DynamicPropertyFactory.initWithConfigurationSource(configuration);

    }

    private ServletRegistration.Dynamic addServlet(ServletContext servletContext, String servletName, Servlet servlet,
                                                   int loadOnStartup, DynamicStringProperty urlPattern) {
        return addServlet(servletContext, servletName, servlet, loadOnStartup, urlPattern.get());
    }

    private ServletRegistration.Dynamic addServlet(ServletContext servletContext, String servletName, Servlet servlet,
        int loadOnStartup, String urlPattern) {
            ServletRegistration.Dynamic reg = servletContext.addServlet(servletName, servlet);
        Assert.notNull(reg, "Unable to create servlet "+servletName);
        reg.setLoadOnStartup(loadOnStartup);
        Set<String> mappingConflicts = reg.addMapping(urlPattern);

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
