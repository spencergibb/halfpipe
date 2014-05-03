package halfpipe.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import halfpipe.consul.client.KVClient;
import halfpipe.consul.model.KeyValue;
import halfpipe.logging.Log;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: spencergibb
 * Date: 5/2/14
 * Time: 11:59 PM
 */
public class RouteCache {

    public static final String DEFAULT_ROUTE = "/";
    @Log
    Logger logger;

    @Inject
    KVClient kvClient;

    @Inject
    ObjectMapper objectMapper;

    AtomicReference<LinkedHashMap<String, String>> routesRef = new AtomicReference<>(new LinkedHashMap<String, String>());

    public LinkedHashMap<String, String> getRoutes() {
        return routesRef.get();
    }

    @Scheduled(fixedDelayString = "${router.cacheRefresh}")
    public void refresh() throws IOException {
        logger.info("Starting refresh of route cache");
        LinkedHashMap<String, String> routes = new LinkedHashMap<>();

        List<KeyValue> routing = kvClient.getKeyValueRecurse("routing");

        String defaultServiceId = null;

        for (KeyValue routeDef : routing) {
            String[] parts = routeDef.getKey().split(DEFAULT_ROUTE);

            if (parts.length == 2) {
                String serviceId = parts[1];
                List<String> serviceRoutes = objectMapper.readValue(routeDef.getDecoded(), List.class);
                for (String route : serviceRoutes) {
                    if (route.equals(DEFAULT_ROUTE)) {
                        if (defaultServiceId != null) {
                            logger.warn("Default route already defined by {}", serviceId);
                        }
                        defaultServiceId = serviceId;
                    } else {
                        if (routes.containsKey(route)) {
                            logger.warn("Routes contains entry for {}: routes {}", route, routes.get(route));
                        }
                        routes.put(route, serviceId);
                    }
                }
            } else {
                logger.warn("Invalid route definition key {}, routes {}",
                        routeDef.getKey(), routeDef.getDecoded());
            }
        }

        if (defaultServiceId != null) {
            routes.put(DEFAULT_ROUTE, defaultServiceId);
        }

        routesRef.set(routes);

        logger.info("Completed refresh of route cache");
    }
}
