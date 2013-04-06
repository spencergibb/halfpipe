package halfpipe.web;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/3/13
 * Time: 12:19 PM
 */
public interface ServletContextHandler {
    /**
     * Allow direct access to the Servlet Context
     * @return
     */
    public ServletContext getServletContext();

    /**
     * Convenience method for adding a servlet
     *
     * @param name
     * @param servlet
     * @param initParams
     * @param viewPatterns
     * @return
     */
    public Object addServlet(String name, Servlet servlet, Map<String, String> initParams, String... viewPatterns);

    /**
     * Convenience method for adding a servlet filter
     *
     * @param name
     * @param filter
     * @param initParams
     * @param urlPatterns
     * @return
     */
    public Object addFilter(String name, Filter filter, Map<String, String> initParams, String... urlPatterns);
}
