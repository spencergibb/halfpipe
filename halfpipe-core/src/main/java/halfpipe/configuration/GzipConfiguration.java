package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import com.google.common.collect.ImmutableSet;
import com.netflix.config.DynamicBooleanProperty;
import halfpipe.util.Size;

import javax.validation.constraints.NotNull;

@SuppressWarnings("UnusedDeclaration")
public class GzipConfiguration {
    public DynaProp<Boolean> enabled = prop(true);

    @NotNull
    public DynaProp<Size> minimumEntitySize = prop(Size.bytes(256));

    public DynaProp<Size> bufferSize = prop(Size.kilobytes(8));

    //TODO: handle set props public ImmutableSet<String> excludedUserAgents = ImmutableSet.of();

    //TODO: handle set props public ImmutableSet<String> compressedMimeTypes = ImmutableSet.of();
}
