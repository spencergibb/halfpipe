package filters.pre

import com.netflix.zuul.ZuulFilter

/*
 * Copyright 2013 Netflix, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
import com.netflix.zuul.context.RequestContext
import groovy.json.JsonSlurper

import javax.servlet.http.Cookie

/**
 * @author mhawthorne
 */
class PreDecorationFilter extends ZuulFilter {

    static def hosts = [
            dvm: "http://apiData.local.domo.com:19700",

            demo: "https://demo.maestro.domosoftware.net:8443",
            dev: "https://development.maestro.domosoftware.net:8443",
            dev3: "https://dev3.maestro.domosoftware.net:8443",
            fdev: "https://frdevelopment.maestro.domosoftware.net:8443",
            stg: "https://staging.maestro.domosoftware.net:8443",
            fstg: "https://frstaging.maestro.domosoftware.net:8443",
            prd: "https://production.maestro.domosoftware.net:8443",
            fprd: "https://frproduction.maestro.domosoftware.net:8443",

            dev1: "https://us-east-1-dev1-loadbalancer.dev.domo.com:8443",
            load1: "https://us-east-1-load1-loadbalancer.dev.domo.com:8443",
            frdev1: "https://us-east-1-frdev1-loadbalancer.frdev.domo.com:8443",
            stage1: "https://us-east-1-stage1-loadbalancer.stage.domo.com:8443",
            frstage1: "https://us-east-1-frstage1-loadbalancer.frstage.domo.com:8443"
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

        if(!ctx.getRequest().getRequestURI().startsWith("/api/data/v1/oauth")) {
            return null;
        }

        String env;

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
            ctx.setRouteHost(new URL(hosts.get("dvm")));
            ctx.addOriginResponseHeader("X-DOMO-OAuth", env);
        }
    }
}
