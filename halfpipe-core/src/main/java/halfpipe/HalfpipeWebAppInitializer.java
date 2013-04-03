package halfpipe;

import static halfpipe.Application.config;
import static halfpipe.Application.startApplication;
import static halfpipe.HalfpipeConfiguration.rootContext;
import static halfpipe.cli.HalfpipeServer.configureWebApp;

import halfpipe.configuration.Configuration;
import halfpipe.logging.Log;
import halfpipe.logging.LoggingUtils;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.*;
import java.util.Map;
import java.util.Set;

/**
 * http://rockhoppertech.com/blog/spring-mvc-configuration-without-xml/
 */
public abstract class HalfpipeWebAppInitializer<C> extends ContextAware<C>
        implements WebApplicationInitializer, WebRegistrar<ServletContext>
{
    private static final Log LOG = Log.forThisClass();

    static {
        LoggingUtils.bootstrap();
    }

    public HalfpipeWebAppInitializer() {
    }

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        try {
            String configFile = System.getProperty("halfpipe.config.file");
            startApplication(configFile, getContextClass(), getViewContext(), LOG);

            Configuration config = config(rootContext);

            configureWebApp(sc, sc, this, config, false);
        } catch (Exception e) {
            e.printStackTrace();
            sc.log("Unable to initialize Halfpipe web application", e);
            throw new ServletException("Unable to initialize Halfpipe web application", e);
        }
    }

    public ServletRegistration.Dynamic addServlet(ServletContext servletContext, String servletName, Servlet servlet,
        String urlPattern, Map<String, String> initParams)
    {
        ServletRegistration.Dynamic reg = servletContext.addServlet(servletName, servlet);
        Assert.notNull(reg, "Unable to create servlet " + servletName);
        reg.setLoadOnStartup(1); //TODO: config loadOnStartup?

        reg.setInitParameters(initParams);

        Set<String> mappingConflicts = reg.addMapping(urlPattern);
        if (!mappingConflicts.isEmpty()) {
            for (String s : mappingConflicts) {
                LOG.warn("Mapping conflict: {}", s);
            }
            throw new IllegalStateException(
                    "'"+servletName+"' cannot be mapped to '/' under Tomcat versions <= 7.0.14");
        }

        return reg;
    }

    public FilterRegistration.Dynamic addFilter(ServletContext servletContext, String filterName, Filter filter,
                                                String urlPattern, Map<String, String> initParams) {
        FilterRegistration.Dynamic fr = servletContext.addFilter(filterName, filter);
        Assert.notNull(fr, "Unable to create filter "+filterName);
        fr.addMappingForUrlPatterns(null, true, urlPattern);
        fr.setInitParameters(initParams);
        return fr;
    }
}
