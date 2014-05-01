package halfpipe.swagger;

import halfpipe.web.EndpointDynaProps;
import halfpipe.properties.ArchaiusProperties;
import halfpipe.properties.DynaProp;
import lombok.Data;

import static halfpipe.properties.DynamicProp.*;

/**
 * User: spencergibb
 * Date: 4/21/14
 * Time: 4:02 PM
 */
@ArchaiusProperties("swagger")
@Data
public class SwaggerProperties extends EndpointDynaProps {
    protected DynaProp<String> id = prop("swagger");
    private DynaProp<String> apiVersion = prop("0");
    private DynaProp<String> basePath;
    private DynaProp<String> staticPath = prop("swaggerfiles");
    private DynaProp<String> templateName = prop("swagger");
}
