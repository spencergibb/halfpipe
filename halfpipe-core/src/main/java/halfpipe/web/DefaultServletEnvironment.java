package halfpipe.web;

import halfpipe.logging.Log;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import javax.servlet.*;
import java.util.Map;
import java.util.Set;

/**
 * User: spencergibb
 * Date: 4/5/13
 * Time: 10:55 AM
 */
public class DefaultServletEnvironment implements ServletEnvironment {
    private static final Logger LOG = Log.forThisClass();

    ServletContext context;

    public DefaultServletEnvironment(ServletContext context) {
        this.context = context;
    }

    public ServletContext getServletContext() {
        return context;
    }

    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet,
                                                  Map<String, String> initParams, String... viewPatterns)
    {
        ServletRegistration.Dynamic reg = context.addServlet(servletName, servlet);
        Assert.notNull(reg, "Unable to create servlet " + servletName);
        reg.setLoadOnStartup(1); //TODO: config loadOnStartup?

        reg.setInitParameters(initParams);
        Set<String> mappingConflicts = reg.addMapping(viewPatterns);
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
                                                Map<String, String> initParams, String... urlPatterns) {
        FilterRegistration.Dynamic fr = context.addFilter(filterName, filter);
        Assert.notNull(fr, "Unable to create filter " + filterName);
        fr.addMappingForUrlPatterns(null, true, urlPatterns);
        fr.setInitParameters(initParams);
        return fr;
    }

    @Override
    public boolean isRegisterDefault() {
        return false;
    }
}
