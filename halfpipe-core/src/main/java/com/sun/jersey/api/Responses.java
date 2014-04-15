package com.sun.jersey.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class Responses {
    public static final int NO_CONTENT = 204;

    public static final int NOT_MODIFIED = 304;

    public static final int CLIENT_ERROR = 400;

    public static final int NOT_FOUND = 404;

    public static final int METHOD_NOT_ALLOWED = 405;

    public static final int NOT_ACCEPTABLE = 406;

    public static final int CONFLICT = 409;

    public static final int PRECONDITION_FAILED = 412;

    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    public static ResponseBuilder noContent() {
        return status(NO_CONTENT);
    }

    public static ResponseBuilder notModified() {
        return status(NOT_MODIFIED);
    }

    public static ResponseBuilder clientError() {
        return status(CLIENT_ERROR);
    }

    public static ResponseBuilder notFound() {
        return status(NOT_FOUND);
    }

    public static ResponseBuilder methodNotAllowed() {
        return status(METHOD_NOT_ALLOWED);
    }

    public static ResponseBuilder notAcceptable() {
        return status(NOT_ACCEPTABLE);
    }

    public static ResponseBuilder conflict() {
        return status(CONFLICT);
    }

    public static ResponseBuilder preconditionFailed() {
        return status(PRECONDITION_FAILED);
    }

    public static ResponseBuilder unsupportedMediaType() {
        return status(UNSUPPORTED_MEDIA_TYPE);
    }

    private static ResponseBuilder status(int status) {
        return Response.status(status);
    }
}
