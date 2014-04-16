package com.sun.jersey.api;

import javax.ws.rs.WebApplicationException;
import java.net.URI;

public class NotFoundException extends WebApplicationException {

    private final URI notFoundUri;

    /**
     * Create a HTTP 404 (Not Found) exception.
     */
    public NotFoundException() {
        this((URI)null);
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     *
     * @param notFoundUri the URI that cannot be found.
     */
    public NotFoundException(URI notFoundUri) {
        super(Responses.notFound().build());
        this.notFoundUri = notFoundUri;
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     *
     * @param message the String that is the entity of the 404 response.
     */
    public NotFoundException(String message) {
        this(message, null);
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     *
     * @param message the String that is the entity of the 404 response.
     * @param notFoundUri the URI that cannot be found.
     */
    public NotFoundException(String message, URI notFoundUri) {
        super(Responses.notFound().
                entity(message).type("text/plain").build());
        this.notFoundUri = notFoundUri;
    }

    /**
     * Get the URI that is not found.
     *
     * @return the URI that is not found.
     */
    public URI getNotFoundUri() {
        return notFoundUri;
    }
}
