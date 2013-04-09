package halfpipe.configuration;

import static halfpipe.configuration.Defaults.*;

import com.google.common.collect.ImmutableSet;
import com.netflix.config.DynamicBooleanProperty;
import halfpipe.util.Size;

import javax.validation.constraints.NotNull;

@SuppressWarnings("UnusedDeclaration")
public class GzipConfiguration {
    public DynamicBooleanProperty enabled = prop(true);

    @NotNull
    public DynamicProp<Size> minimumEntitySize = prop(Size.bytes(256));

    public DynamicProp<Size> bufferSize = prop(Size.kilobytes(8));

    //TODO: handle set props public ImmutableSet<String> excludedUserAgents = ImmutableSet.of();

    //TODO: handle set props public ImmutableSet<String> compressedMimeTypes = ImmutableSet.of();
}
