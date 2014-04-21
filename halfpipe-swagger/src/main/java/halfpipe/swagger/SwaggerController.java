package halfpipe.swagger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.model.ApiListingReference;
import com.wordnik.swagger.model.ResourceListing;
import com.wordnik.swagger.reader.ClassReader;
import com.wordnik.swagger.reader.ClassReaders;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scala.Option;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.transform;

/**
 * User: spencergibb
 * Date: 4/17/14
 * Time: 5:01 PM
 */
@Controller
public class SwaggerController {

    @Inject
    ApplicationContext context;

    @Inject
    SwaggerConfig swaggerConfig;

    @Inject
    SwaggerProperties props;

    @RequestMapping("/swagger")
    public String swagger(Map<String, Object> model) {
        model.put("apiUrl", props.getApiUrl().get());
        return "swagger";
    }

    //TODO: configurable path?
    @RequestMapping(value = "/api-docs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

    private List<ApiListing> gatherListings() {
        Map<String, Object> endpoints = context.getBeansWithAnnotation(Path.class);

        ClassReader classReader = ClassReaders.reader().get();

        List<ApiListing> listings = new ArrayList<>();
        for (Object endpoint : endpoints.values()) {
            //TODO: derive /api-docs string
            Option<ApiListing> listing = classReader.read("/api-docs", endpoint.getClass(), swaggerConfig);
            if (listing.nonEmpty()) {
                listings.add(listing.get());
            }
        }

        return listings;
    }

    public static <T> scala.collection.immutable.List<T> scalaList(Iterable<T> javaList) {
        return scala.collection.JavaConversions.asScalaIterable(javaList).toList();
    }

    @RequestMapping(value = "/api-docs/{route}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ApiListing resourceListing(@PathVariable String route) {
        final String path = cleanRoute(route);
        Iterable<ApiListing> listings = filter(gatherListings(), new Predicate<ApiListing>() {
            @Override
            public boolean apply(@Nullable ApiListing input) {
                String listingPath = input.resourcePath();
                if (!listingPath.startsWith("/"))
                    listingPath = "/"+listingPath;
                return path.equals(listingPath);
            }
        });

        ApiListing listing = getFirst(listings, null);

        if (listing == null) {
            throw new ResourceNotFoundException();
        }
        return listing;
    }

    private String cleanRoute(String route) {
        String cleanStart = (route.startsWith("/")) ? route : "/"+route ;

        if (cleanStart.endsWith("/")) {
            return cleanStart.substring(0, cleanStart.length() - 1);
        }
        return cleanStart;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {}
}
