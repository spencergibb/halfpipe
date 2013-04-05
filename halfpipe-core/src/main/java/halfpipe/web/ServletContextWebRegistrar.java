package halfpipe.web;

import halfpipe.logging.Log;
import org.springframework.util.Assert;

import javax.servlet.*;
import java.util.Map;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 10:55 AM
 */
public class ServletContextWebRegistrar implements WebRegistrar<ServletContext> {
    private static final Log LOG = Log.forThisClass();

    ServletContext context;

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet,
                                                  String urlPattern, Map<String, String> initParams)
    {
        ServletRegistration.Dynamic reg = context.addServlet(servletName, servlet);
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

    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter,
                                                String urlPattern, Map<String, String> initParams) {
        FilterRegistration.Dynamic fr = context.addFilter(filterName, filter);
        Assert.notNull(fr, "Unable to create filter " + filterName);
        fr.addMappingForUrlPatterns(null, true, urlPattern);
        fr.setInitParameters(initParams);
        return fr;
    }

}
