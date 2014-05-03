package filters.pre

import com.netflix.zuul.context.RequestContext
import halfpipe.router.RouteCache
import halfpipe.router.SpringFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author mhawthorne
 */
class PreDecorationFilter extends SpringFilter {
    private static Logger LOG = LoggerFactory.getLogger(PreDecorationFilter.class);

    @Override
    int filterOrder() {
        return 5
    }

    @Override
    String filterType() {
        return "pre"
    }

    @Override
    boolean shouldFilter() {
        return true;
    }

    @Override
    Object run() {
        def routeCache = getBean(RouteCache.class)

        RequestContext ctx = RequestContext.getCurrentContext()

        def requestURI = ctx.getRequest().getRequestURI()

        def serviceId = null;
        def routes = routeCache.getRoutes()
        routes.keySet().find { path ->
            //TODO: use ant matchers?
            if (requestURI.startsWith(path)) {
                serviceId = routes[path]
                return true
            }
            return false
        }

        if (serviceId != null) {
            // set serverId for use in filters.route.RibbonRequest
            ctx.set("serviceId", serviceId)
            ctx.setRouteHost(null)
            ctx.addOriginResponseHeader("X-Halfpipe-ServiceId", serviceId);
        }
    }
}
