package halfpipe.example.properties;

import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

import static halfpipe.properties.DynamicProp.*;
import static com.google.common.collect.Lists.*;

/**
 * User: spencergibb
 * Date: 12/12/13
 * Time: 4:43 PM
 */
@Component
@ArchaiusProperties("hello")
@Data
public class HelloProperties {

    private DynaProp<String> defaultMessage = prop("HelloWorld");

    private DynaProp<Integer> myNumber = prop(1);

    private DynaProp<List<String>> myThings = prop(newArrayList("z", "y"));
}
