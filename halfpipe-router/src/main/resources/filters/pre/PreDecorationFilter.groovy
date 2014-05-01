package filters.pre

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import groovy.json.JsonSlurper
import org.apache.commons.codec.binary.Base64

import javax.servlet.http.Cookie

/**
 * @author mhawthorne
 */
class PreDecorationFilter extends ZuulFilter {

    static def hosts = [
            defaulthost: "http://127.0.0.1:8080",
    ]

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

        RequestContext ctx = RequestContext.getCurrentContext()

        if(!ctx.getRequest().getRequestURI().startsWith("/")) {
            return null;
        }

        String env = "defaulthost";

        // first attempt to get the environment from the state
        def params = ctx.getRequestQueryParams();

        if(params != null && params.get("state") != null) {
            String encoded = (String) params.get("state")
            String decoded = new String(Base64.decodeBase64(encoded))

            def slurper = new JsonSlurper()
            def result = slurper.parseText(decoded)
            env = result.e
        }

        // if it isn't there, get the state from the cookie
        def request = ctx.getRequest();

        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("e".equals(cookie.getName())) {
                    env = cookie.getValue();
                    break;
                }
            }
        }

        // route if a value was found in the request
        if(env != null) {
            ctx.setRouteHost(new URL(hosts.get(env)));
            ctx.addOriginResponseHeader("X-Env", env);
        }
    }
}
