package halfpipe.swagger;

import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import halfpipe.web.EndpointDynaProps;
import lombok.Data;

import static halfpipe.properties.DynamicProp.prop;

/**
 * User: spencergibb
 * Date: 5/2/14
 * Time: 8:59 AM
 */
@ArchaiusProperties("swagger.endpoint")
@Data
public class SwaggerEndpointProps extends EndpointDynaProps {
    protected DynaProp<String> id = prop("swagger");
}
