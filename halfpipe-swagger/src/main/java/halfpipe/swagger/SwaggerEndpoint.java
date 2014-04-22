package halfpipe.swagger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ApiListingReference;
import com.wordnik.swagger.model.ResourceListing;
import com.wordnik.swagger.reader.ClassReader;
import com.wordnik.swagger.reader.ClassReaders;
import halfpipe.mvc.EndpointDelegate;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import scala.Option;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.transform;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 5:01 PM
 */
public class SwaggerEndpoint extends EndpointMvcAdapter {
    public static final String API_DOCS = "/api-docs";

    @Inject
    private ApplicationContext context;

    @Inject
    private SwaggerConfig swaggerConfig;

    private final SwaggerProperties properties;

    public SwaggerEndpoint(SwaggerProperties properties) {
        super(new EndpointDelegate(properties));
        this.properties = properties;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public Object invoke() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("apiUrl", getApiUrl());
        model.put("staticPath", cleanRoute(properties.getStaticPath().get()));
        return new ModelAndView(properties.getTemplateName().get(), model);
    }

    @RequestMapping(value = API_DOCS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResourceListing resourcesListing() {
        List<ApiListing> apiListings = gatherListings();
        Iterable<ApiListingReference> listings = transform(apiListings, new Function<ApiListing, ApiListingReference>() {
            @Nullable
            @Override
            public ApiListingReference apply(@Nullable ApiListing apiListing) {
                return new ApiListingReference(apiListing.resourcePath(), apiListing.description(), apiListing.position());
            }
        });

        ResourceListing resourceListing = new ResourceListing(swaggerConfig.apiVersion(),
                swaggerConfig.swaggerVersion(),
                scalaList(listings),
                swaggerConfig.authorizations(),
                swaggerConfig.info());

        return resourceListing;
    }

    @RequestMapping(value = "/api-docs/{route}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ApiListing resourceListing(@PathVariable String route) {
        final String path = cleanRoute(route);
        Iterable<ApiListing> listings = filter(gatherListings(), new Predicate<ApiListing>() {
            @Override
            public boolean apply(@Nullable ApiListing input) {
                String listingPath = input.resourcePath();
                if (!listingPath.startsWith("/"))
                    listingPath = "/" + listingPath;
                return path.equals(listingPath);
            }
        });

        ApiListing listing = getFirst(listings, null);

        if (listing == null) {
            throw new ResourceNotFoundException();
        }
        return listing;
    }

    private List<ApiListing> gatherListings() {
        Map<String, Object> endpoints = context.getBeansWithAnnotation(Path.class);

        ClassReader classReader = ClassReaders.reader().get();

        List<ApiListing> listings = new ArrayList<>();
        for (Object endpoint : endpoints.values()) {
            Option<ApiListing> listing = classReader.read(getApiUrl(), endpoint.getClass(), swaggerConfig);
            if (listing.nonEmpty()) {
                listings.add(listing.get());
            }
        }

        return listings;
    }

    private String getApiUrl() {
        return "/"+properties.getId().get()+ API_DOCS;
    }

    private static <T> scala.collection.immutable.List<T> scalaList(Iterable<T> javaList) {
        return scala.collection.JavaConversions.asScalaIterable(javaList).toList();
    }

    private String cleanRoute(String route) {
        String cleanStart = (route.startsWith("/")) ? route : "/"+route ;

        if (cleanStart.endsWith("/")) {
            return cleanStart.substring(0, cleanStart.length() - 1);
        }
        return cleanStart;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class ResourceNotFoundException extends RuntimeException {}
}
