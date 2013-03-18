package halfpipe.validation;

import com.google.common.collect.ImmutableList;

// see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation
public class InvalidEntityException extends RuntimeException {
    private final ImmutableList<String> errors;

    public InvalidEntityException(String message, Iterable<String> errors) {
        super(message);
        this.errors = ImmutableList.copyOf(errors);
    }

    public ImmutableList<String> getErrors() {
        return errors;
    }
}
