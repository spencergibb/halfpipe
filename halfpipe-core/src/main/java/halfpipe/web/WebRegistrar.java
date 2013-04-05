package halfpipe.web;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.Map;

/**
 * User: spencergibb
 * Date: 4/3/13
 * Time: 12:19 PM
 */
public interface WebRegistrar<WC> {
    public void setContext(WC context);
    public Object addServlet(String name, Servlet servlet, String viewPattern, Map<String, String> initParams);

    public Object addFilter(String name, Filter filter, String urlPattern, Map<String, String> initParams);
}
