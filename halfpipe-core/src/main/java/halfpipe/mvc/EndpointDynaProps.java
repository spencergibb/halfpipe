package halfpipe.mvc;

import halfpipe.properties.DynaProp;
import lombok.Data;

import static halfpipe.properties.DynamicProp.*;

/**
 * User: spencergibb
 * Date: 4/22/14
 * Time: 9:43 AM
 */
@Data
public class EndpointDynaProps {
    protected DynaProp<String> id;
    protected DynaProp<Boolean> enabled = prop(Boolean.TRUE);
    protected DynaProp<Boolean> sensitive = prop(Boolean.FALSE);
}
