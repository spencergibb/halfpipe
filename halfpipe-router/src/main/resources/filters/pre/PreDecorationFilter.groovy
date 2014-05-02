package filters.pre

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.context.RequestContext
import halfpipe.consul.client.CatalogClient
import halfpipe.consul.client.KVClient
import halfpipe.router.SpringFilter

/**
 * @author mhawthorne
 */
class PreDecorationFilter extends SpringFilter {

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
        def kvClient = getBean(KVClient.class)
        def objectMapper = getBean(ObjectMapper.class)

        def routingList = kvClient.getKeyValueRecurse("routing")

        //TODO: cache w/polling or use archaius callback
        def routes = new LinkedHashMap<String, String>() //preserve ordering
        def defaultServiceId = null;

        routingList.each { routeDef ->
            def key = routeDef.key
            def parts = key.tokenize('/');
            if (parts.size() == 2) {
                def serviceId = parts[1]
                def decoded = routeDef.decoded
                def serviceRoutes = objectMapper.readValue(decoded, List.class)
                serviceRoutes.each {
                    if (it == "/") {
                        if (defaultServiceId != null) {
                            println("Warning default route already defined by ${serviceId}")
                        }
                        defaultServiceId = serviceId
                    } else {
                        if (routes.containsKey(it)) {
                            println("Warning routes contains entry for ${it}: "+routes[it])
                        }
                        routes[it] = serviceId
                    }
                }
            } else {
                //TODO: log warning
            }
        }

        if (defaultServiceId) {
            routes["/"] = defaultServiceId
        }

        RequestContext ctx = RequestContext.getCurrentContext()


        def requestURI = ctx.getRequest().getRequestURI()

        def serviceId = null;
        routes.keySet().find { path ->
            //TODO: use ant matchers?
            if (requestURI.startsWith(path)) {
                serviceId = routes[path]
                return true
            }
            return false
        }

        // ctx.getRequestQueryParams();
        //def request = ctx.getRequest();

        if (serviceId != null) {
            //TODO: use ribbon with serviceId and consul server lookup
            def catalogClient = getBean(CatalogClient.class)
            def nodes = catalogClient.getServiceNodes(serviceId)

            def node = nodes.find()

            // route if a node was found
            if (node) {
                //TODO: use tags for https
                def url = "http://${node.address}:${node.servicePort}"
                ctx.setRouteHost(new URL(url));
                ctx.addOriginResponseHeader("X-Halfpipe-ServiceId", serviceId);
            }
        }
    }
}
