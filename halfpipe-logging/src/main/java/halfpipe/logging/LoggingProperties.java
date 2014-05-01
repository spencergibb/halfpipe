package halfpipe.logging;

import ch.qos.logback.classic.Level;
import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static halfpipe.properties.DynamicProp.*;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 3:12 AM
 */
@Component
@ArchaiusProperties("logging")
@Data
public class LoggingProperties {
    private static final List<String> EMPTY = new ArrayList<>();

    private DynaProp<List<String>> loggers = prop(EMPTY);
    private DynaProp<Level> level = prop(Level.INFO);
}
